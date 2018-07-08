package com.atguigu.chinarallway.Bean;

import java.sql.Date;

/**
 * Created by Kiboooo on 2017/10/13.
 * 生产计划Beam
 */

public class Task {

        private Date TaskDate;
        private String BName;
        private String BID;
        private String MakeOrder;
        private String MakePosID;
        private int PedID;
        private String Pos;
        private int Permit;

        public Date getTaskDate() {
            return TaskDate;
        }
        public void setTaskDate(Date taskDate) {
            TaskDate = taskDate;
        }
        public String getBName() {
            return BName;
        }
        public void setBName(String bName) {
            BName = bName;
        }
        public String getBID() {
            return BID;
        }
        public void setBID(String bID) {
            BID = bID;
        }
        public String getMakeOrder() {
            return MakeOrder;
        }
        public void setMakeOrder(String makeOrder) {
            MakeOrder = makeOrder;
        }
        public String getMakePosID() {
            return MakePosID;
        }
        public void setMakePosID(String makePosID) {
            MakePosID = makePosID;
        }
        public int getPedID() {
            return PedID;
        }
        public void setPedID(int pedID) {
            PedID = pedID;
        }
        public String getPos() {
            return Pos;
        }
        public void setPos(String pos) {
            Pos = pos;
        }
        public int getPermit() {
            return Permit;
        }
        public void setPermit(int permit) {
            Permit = permit;
        }
   }

