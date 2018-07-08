package com.atguigu.chinarallway.Activity

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.support.design.widget.Snackbar
import android.support.v4.content.FileProvider
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.atguigu.chinarallway.Bean.AllStaticBean
import com.atguigu.chinarallway.Bean.BeamData
import com.atguigu.chinarallway.Bean.FilesData
import com.atguigu.chinarallway.BuildConfig
import com.atguigu.chinarallway.Dialog.LoadingDialog
import com.atguigu.chinarallway.R
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_browse_video.*
import kotlinx.android.synthetic.main.common_title2.*
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONObject
import java.io.File
import java.net.URLEncoder
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * 查看工艺视频
 */

class BrowseVideoActivity : AppCompatActivity(), View.OnClickListener {
    private val VIDEO: Int = 8945
    val Beamlist_Name = LinkedList<String>()
    val Beamlist_LR = LinkedList<String>()
    val Beamlist_ID = LinkedList<String>()


    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.iv_back->
                    finish()
        }
    }

    var files:MutableList<FilesData> = LinkedList()
    var beam:BeamData? = null
    var fileData:FilesData? = null
    var progress:LoadingDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browse_video)
        tv_title_table.text = "查看工艺视频"
        iv_back.setOnClickListener(this)
        initData()
        downloadButton()
    }

    fun showProgress(msg:String){
        progress = LoadingDialog(this,msg)
        progress?.show()
    }

    fun dissmisDialog(){
        progress?.close()
        progress = null
    }

    fun downloadButton(){
        download_button.setOnClickListener {
            if(fileData==null){
                Snackbar.make(video_select,"请您先选择视频",2000).show()
                return@setOnClickListener
            }
            showProgress("请稍后，正在下载视频")
            downloadVideo()
        }
        look_download_dir.setOnClickListener{
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            val file = File(AllStaticBean.downloadVideoDir)
            if(!file.exists())
                file.mkdir()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                intent.addCategory(Intent.CATEGORY_DEFAULT)
                intent.setDataAndType(FileProvider.getUriForFile(applicationContext,
                        BuildConfig.APPLICATION_ID + ".fileprovider", file)
                        , "video/mp4")
            } else {
                intent.setDataAndType(Uri.fromFile(file), "video/mp4")
                intent.addCategory(Intent.CATEGORY_DEFAULT)
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            startActivityForResult(intent, VIDEO)
        }
    }

    //获取真实地址
    private fun getVideoPath(uri: Uri, selection: String?): String? {
        var path: String? = null
        val cursor = contentResolver.query(uri, null, selection, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA))
            }
            cursor.close()
        }
        return path
    }

    //回调，打开选择的视频
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    @TargetApi(Build.VERSION_CODES.KITKAT)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e("onActivityResult", "  $resultCode   $requestCode")

        if (requestCode == VIDEO && resultCode == Activity.RESULT_OK) {
            val uri = data!!.data
            if (DocumentsContract.isDocumentUri(this, uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val id = docId.split(":")[1]
                val selection = MediaStore.Video.Media._ID + "=" + id
                val real_path = getVideoPath(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, selection)
                Log.e("onActivityResult", uri.path + uri.toString() + " 真  " + real_path)
                val intent = Intent(Intent.ACTION_VIEW)
                //必须使用回调的真实绝对地址
                intent.setDataAndType(Uri.parse(real_path), "video/*")
                Log.e("onActivityResult", Uri.parse(real_path).toString())
                startActivity(intent)
            }else if ("content" == uri.scheme) {
                //“content://media/external/images/media/8302” 这种Url 可以直接获取到相应表，获取绝对地址
                val real_path = getVideoPath(uri, null)
                Log.e("onActivityResult", uri.path + uri.toString() + " 真  " + real_path)
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setDataAndType(Uri.parse(real_path), "video/*")
                Log.e("onActivityResult", Uri.parse(real_path).toString())
                startActivity(intent)
            }
        }
    }

    fun downloadVideo(){
        doAsync {
            val fileName = fileData!!.name
            val cilent = OkHttpClient.Builder()
                    .connectTimeout(15,TimeUnit.SECONDS)
                    .build()
            val body = FormBody.Builder()
                    .add("type","video")
                    .add("fileName",URLEncoder.encode(fileName,"UTF-8"))
                    .build()
            val request = Request.Builder()
                    .url(AllStaticBean.URL+AllStaticBean.FileDownLoadURL)
                    .post(body)
                    .build()
            val call = cilent.newCall(request)
            val response =  call.execute()
            val videoDir = File(AllStaticBean.downloadVideoDir)
            if(!videoDir.exists())
                videoDir.mkdir()
            val file = File(videoDir.absolutePath+File.separator+fileName)
            if(response.isSuccessful){
                val input = response.body().byteStream()
                val output = file.outputStream()
                val bytes = ByteArray(4096)
                while(true){
                    val count = input.read(bytes)
                    if(count == -1)
                        break
                    output.write(bytes,0,count)
                }
                output.close()
                response.body().close()
                uiThread { dissmisDialog();downloadOK(file) }
            }else
            {
                println(response.body().string())
                uiThread { dissmisDialog();Snackbar.make(video_select,"下载失败",2000).show() }
            }
        }
    }

    fun downloadOK(file: File){
        val dialog = AlertDialog.Builder(this)
                .setMessage("文件下载成功，是否打开文件")
                .setPositiveButton("确定"){
                    dialog, which ->
                    val uri = Uri.parse(file.absolutePath)
                    Log.e("文件下载成功", file.absolutePath+"    "+uri.toString())
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.setDataAndType(uri,"video/*")
                    startActivity(intent)
                }
                .setNegativeButton("取消",null)
                .create()
        dialog.show()
    }

    fun initData(){
        showProgress("请稍后正在加载数据")
        doAsync {
            requestData()
        }
    }

    @SuppressLint("WrongConstant")
    fun requestData(){
        var resultCode = 0
        val client = OkHttpClient.Builder()
                .connectTimeout(10,TimeUnit.SECONDS)
                .readTimeout(10,TimeUnit.SECONDS)
                .build()
        var body = FormBody.Builder()
                .add("uid",LoginActivity.userData.uid)
                .add("type","beam")
                .add("searchType","")
                .add("searchParam","")
                .add("searchAll","1")
                .build()
        var request = Request.Builder()
                .header("Cookie",AllStaticBean.mRespenseCookie)
                .url(AllStaticBean.URL+AllStaticBean.GroupURL)
                .post(body)
                .build()

        var call = client.newCall(request)
        var response = call.execute()
        if (response.isSuccessful) {
            val obj = JSONObject(response.body().string())
            if (obj.getInt("code") > 0) {
                val array = obj.getJSONArray("data")
                val beam = Gson().fromJson(array.toString(), Array<BeamData>::class.java)
                AllStaticBean.MaxBeamData = beam
                runOnUiThread { dissmisDialog();initBeamSpinner() }
            } else {
                runOnUiThread { dissmisDialog();Toast.makeText(this@BrowseVideoActivity, "暂无数据${obj.getInt("code")}.${response.code()}", 2000).show() }
            }
        } else {
            runOnUiThread { dissmisDialog();Toast.makeText(this@BrowseVideoActivity, "请求梁板数据失败", 2000).show() }
        }
        Log.e("线程","jxxxx")
        body = FormBody.Builder()
                .add("bID", "  ")
                .add("bName", "  ")
                .add("all", "1")
                .add("type", "video")
                .build()

        request = Request.Builder()
                .url(AllStaticBean.URL+AllStaticBean.FileSearchURL)
                .post(body)
                .build()

        call = client.newCall(request)
        response = call.execute()
        if(response.isSuccessful){
            val obj = JSONObject(response.body().string())
            println(obj.toString())
            if(obj.getInt("code")>0){
                val array = obj.getJSONArray("data")
                val files =Gson().fromJson(array.toString(),Array<FilesData>::class.java)
                this.files = files.toMutableList()
                runOnUiThread { dissmisDialog();}
            }else{
                runOnUiThread {
                    dissmisDialog()
                    Toast.makeText(this@BrowseVideoActivity,"暂无数据${obj.getInt("code")}.${response.code()}",
                            2000).show()
                }
            }
        }else{
            runOnUiThread { dissmisDialog();Toast.makeText(this@BrowseVideoActivity,
                    "请求视频数据失败",2000).show() }
        }
    }

    fun initBeamSpinner(){
        Beamlist_Name.clear()
        Beamlist_Name.add("")
        AllStaticBean.MaxBeamData.forEach {
            if(Beamlist_Name.indexOf(it.getbName())<0)
            Beamlist_Name.add(it.getbName())
        }

        val adapter = ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,Beamlist_Name)
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        beam_select.adapter = adapter
        beam_select.onItemSelectedListener = object:AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Beamlist_LR.clear()
                Beamlist_ID.clear()
                Beamlist_LR.add("")
                onBindSelcetLR(Beamlist_Name[position])
            }

        }

    }
    fun onBindSelcetLR(beamName : String) {
        Beamlist_ID.add("")
        AllStaticBean.MaxBeamData.forEach {
            if (beamName == it.getbName() && Beamlist_LR.indexOf(it.getbID().substring(0, 2)) < 0) {
                Beamlist_LR.add(it.getbID().substring(0, 2))
            }
        }
        val adapter = ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,Beamlist_LR)
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        beam_LR_select.adapter = adapter
        beam_LR_select.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                onBindSelcetID(beamName,Beamlist_LR[position])
            }
        }
    }

    fun onBindSelcetID(beamName: String, LR: String) {
        AllStaticBean.MaxBeamData.forEach {
            if (beamName == it.getbName() && LR == it.getbID().substring(0, 2)) {
                Beamlist_ID.add(it.getbID().substring(2))
            }
        }
        val adapter = ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,Beamlist_ID)
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        beam_ID_select.adapter = adapter
        beam_ID_select.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (files.size==0)
                    return

                val FindId = LR+Beamlist_ID[position]
                Log.e("cc   ",""+FindId)
                val videoList = files.filter { it.getbName() == beamName && it.getbID() == FindId }

                val videoTextList = LinkedList<String>()
                videoList.forEach { videoTextList.add(it.name) }

                val videoAdapter = ArrayAdapter<String>(this@BrowseVideoActivity,R.layout
                        .support_simple_spinner_dropdown_item,videoTextList)
                video_select.adapter = videoAdapter
                video_select.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        this@BrowseVideoActivity.fileData = videoList[position]
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}

                }
            }

        }
    }

}
