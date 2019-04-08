package com.ns.networking.model;

import java.util.List;

public class TCModel {


    /**
     * resultcode : 1
     * message : Static Data
     * data : [{"screen":"SIGNUP","screenContent":"The terms of this service will be governed by the terms and conditions of the Stellarjet Membership Agreement which I have read and agreed to."},{"screen":"BOOKING","screenContent":"I have read and agree to the terms mentioned in the flight service agreement."},{"screen":"BOARDING","screenContent":"I have read and agree to the terms & I declare that I am not carrying any of the dangerous and prohibited goods stipulated by DGCA."}]
     */

    private int resultcode;
    private String message;
    private List<DataBean> data;

    public int getResultcode() {
        return resultcode;
    }

    public void setResultcode(int resultcode) {
        this.resultcode = resultcode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * screen : SIGNUP
         * screenContent : The terms of this service will be governed by the terms and conditions of the Stellarjet Membership Agreement which I have read and agreed to.
         */

        private String screen;
        private String screenContent;

        public String getScreen() {
            return screen;
        }

        public void setScreen(String screen) {
            this.screen = screen;
        }

        public String getScreenContent() {
            return screenContent;
        }

        public void setScreenContent(String screenContent) {
            this.screenContent = screenContent;
        }
    }
}
