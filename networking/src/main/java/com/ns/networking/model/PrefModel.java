package com.ns.networking.model;

import java.util.List;

public class PrefModel {


    /**
     * main_passenger : {"booking_extra_id":77,"passenger":12,"name":"Mr. Varun RV","phone":"9902609192","membership":"Platinum","seats_info":{"seat_id":12,"seat_code":"Lima"},"pick_address":{"id":15,"address":"ty , Dhaula Kuan, New Delhi, Delhi, India ","address_tag":"thjgo","lat":"28.59612790164899","lng":null},"drop_address":{"id":13,"address":"neben 67/1, HSR Layout, KG Halli, D' Souza Layout, Ashok Nagar, Bengaluru, Karnataka 560002, India","address_tag":"usissisi","lat":null,"lng":null},"boarding_pass_url":"http://dev.stellarjet.com/app/v2/storage/uploads/boardingPasses/ST-12-Varun_RV-20190410130801.pdf","boarding_pass_data":{"seat_name":"Lima","seat_short_code":"L","traveller":"Varun RV","aircraft":"STLR604","date":"16 04 2019","time":"1930","reportTime":"07.20pm","departure":"Tuesday 16 April 07.30pm","from":"Delhi","to":"Bengaluru","from_code":"DEL","to_code":"BLR","stellar_club_code":12,"flight":"S6"},"status":"Confirmed","last_modified_by":"Varun RV","modified_user_type":"Primary","check_in_status":"N","check_in_time":null,"pickup_vehicle_id":null,"pickup_driver_id":null,"drop_vehicle_id":null,"drop_driver_id":null,"food_items":[{"id":6,"name":"Aloo paratha","food_type":"veg","food_type_text":"Vegetarian"}],"user_meta_data":[{"meta_key":"food_prefs","meta_value":[{"food_category":"veg","days":[]},{"food_category":"non-veg","days":["Monday"]},{"food_category":"continental","days":[]}]},{"meta_key":"food_spl_instruction","meta_value":"No such"},{"meta_key":"allergies","meta_value":"On sulphar tablets"},{"meta_key":"lawyer_phone","meta_value":"96325874520"},{"meta_key":"doctor_phone","meta_value":"9863254107"},{"meta_key":"neighbor_phone","meta_value":"9856320147"},{"meta_key":"medications","meta_value":"Paracetamol after Lunch"},{"meta_key":"language_prefs","meta_value":"Kannada, English, Hindi"},{"meta_key":"other_notes","meta_value":"Diabetic Patient."},{"meta_key":"billing_address","meta_value":"Ninestars, Jp nagar, Bangalore"},{"meta_key":"discount_offered_membership","meta_value":"9.00"},{"meta_key":"discount_offered_seat","meta_value":"4.00"}]}
     * co_passengers : []
     */

    private MainPassengerBean main_passenger;
    private List<?> co_passengers;

    public MainPassengerBean getMain_passenger() {
        return main_passenger;
    }

    public void setMain_passenger(MainPassengerBean main_passenger) {
        this.main_passenger = main_passenger;
    }

    public List<?> getCo_passengers() {
        return co_passengers;
    }

    public void setCo_passengers(List<?> co_passengers) {
        this.co_passengers = co_passengers;
    }

    public static class MainPassengerBean {
        /**
         * booking_extra_id : 77
         * passenger : 12
         * name : Mr. Varun RV
         * phone : 9902609192
         * membership : Platinum
         * seats_info : {"seat_id":12,"seat_code":"Lima"}
         * pick_address : {"id":15,"address":"ty , Dhaula Kuan, New Delhi, Delhi, India ","address_tag":"thjgo","lat":"28.59612790164899","lng":null}
         * drop_address : {"id":13,"address":"neben 67/1, HSR Layout, KG Halli, D' Souza Layout, Ashok Nagar, Bengaluru, Karnataka 560002, India","address_tag":"usissisi","lat":null,"lng":null}
         * boarding_pass_url : http://dev.stellarjet.com/app/v2/storage/uploads/boardingPasses/ST-12-Varun_RV-20190410130801.pdf
         * boarding_pass_data : {"seat_name":"Lima","seat_short_code":"L","traveller":"Varun RV","aircraft":"STLR604","date":"16 04 2019","time":"1930","reportTime":"07.20pm","departure":"Tuesday 16 April 07.30pm","from":"Delhi","to":"Bengaluru","from_code":"DEL","to_code":"BLR","stellar_club_code":12,"flight":"S6"}
         * status : Confirmed
         * last_modified_by : Varun RV
         * modified_user_type : Primary
         * check_in_status : N
         * check_in_time : null
         * pickup_vehicle_id : null
         * pickup_driver_id : null
         * drop_vehicle_id : null
         * drop_driver_id : null
         * food_items : [{"id":6,"name":"Aloo paratha","food_type":"veg","food_type_text":"Vegetarian"}]
         * user_meta_data : [{"meta_key":"food_prefs","meta_value":[{"food_category":"veg","days":[]},{"food_category":"non-veg","days":["Monday"]},{"food_category":"continental","days":[]}]},{"meta_key":"food_spl_instruction","meta_value":"No such"},{"meta_key":"allergies","meta_value":"On sulphar tablets"},{"meta_key":"lawyer_phone","meta_value":"96325874520"},{"meta_key":"doctor_phone","meta_value":"9863254107"},{"meta_key":"neighbor_phone","meta_value":"9856320147"},{"meta_key":"medications","meta_value":"Paracetamol after Lunch"},{"meta_key":"language_prefs","meta_value":"Kannada, English, Hindi"},{"meta_key":"other_notes","meta_value":"Diabetic Patient."},{"meta_key":"billing_address","meta_value":"Ninestars, Jp nagar, Bangalore"},{"meta_key":"discount_offered_membership","meta_value":"9.00"},{"meta_key":"discount_offered_seat","meta_value":"4.00"}]
         */

        private int booking_extra_id;
        private int passenger;
        private String name;
        private String phone;
        private String membership;
        private SeatsInfoBean seats_info;
        private PickAddressBean pick_address;
        private DropAddressBean drop_address;
        private String boarding_pass_url;
        private BoardingPassDataBean boarding_pass_data;
        private String status;
        private String last_modified_by;
        private String modified_user_type;
        private String check_in_status;
        private Object check_in_time;
        private Object pickup_vehicle_id;
        private Object pickup_driver_id;
        private Object drop_vehicle_id;
        private Object drop_driver_id;
        private List<FoodItemsBean> food_items;
        private List<UserMetaDataBean> user_meta_data;

        public int getBooking_extra_id() {
            return booking_extra_id;
        }

        public void setBooking_extra_id(int booking_extra_id) {
            this.booking_extra_id = booking_extra_id;
        }

        public int getPassenger() {
            return passenger;
        }

        public void setPassenger(int passenger) {
            this.passenger = passenger;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getMembership() {
            return membership;
        }

        public void setMembership(String membership) {
            this.membership = membership;
        }

        public SeatsInfoBean getSeats_info() {
            return seats_info;
        }

        public void setSeats_info(SeatsInfoBean seats_info) {
            this.seats_info = seats_info;
        }

        public PickAddressBean getPick_address() {
            return pick_address;
        }

        public void setPick_address(PickAddressBean pick_address) {
            this.pick_address = pick_address;
        }

        public DropAddressBean getDrop_address() {
            return drop_address;
        }

        public void setDrop_address(DropAddressBean drop_address) {
            this.drop_address = drop_address;
        }

        public String getBoarding_pass_url() {
            return boarding_pass_url;
        }

        public void setBoarding_pass_url(String boarding_pass_url) {
            this.boarding_pass_url = boarding_pass_url;
        }

        public BoardingPassDataBean getBoarding_pass_data() {
            return boarding_pass_data;
        }

        public void setBoarding_pass_data(BoardingPassDataBean boarding_pass_data) {
            this.boarding_pass_data = boarding_pass_data;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getLast_modified_by() {
            return last_modified_by;
        }

        public void setLast_modified_by(String last_modified_by) {
            this.last_modified_by = last_modified_by;
        }

        public String getModified_user_type() {
            return modified_user_type;
        }

        public void setModified_user_type(String modified_user_type) {
            this.modified_user_type = modified_user_type;
        }

        public String getCheck_in_status() {
            return check_in_status;
        }

        public void setCheck_in_status(String check_in_status) {
            this.check_in_status = check_in_status;
        }

        public Object getCheck_in_time() {
            return check_in_time;
        }

        public void setCheck_in_time(Object check_in_time) {
            this.check_in_time = check_in_time;
        }

        public Object getPickup_vehicle_id() {
            return pickup_vehicle_id;
        }

        public void setPickup_vehicle_id(Object pickup_vehicle_id) {
            this.pickup_vehicle_id = pickup_vehicle_id;
        }

        public Object getPickup_driver_id() {
            return pickup_driver_id;
        }

        public void setPickup_driver_id(Object pickup_driver_id) {
            this.pickup_driver_id = pickup_driver_id;
        }

        public Object getDrop_vehicle_id() {
            return drop_vehicle_id;
        }

        public void setDrop_vehicle_id(Object drop_vehicle_id) {
            this.drop_vehicle_id = drop_vehicle_id;
        }

        public Object getDrop_driver_id() {
            return drop_driver_id;
        }

        public void setDrop_driver_id(Object drop_driver_id) {
            this.drop_driver_id = drop_driver_id;
        }

        public List<FoodItemsBean> getFood_items() {
            return food_items;
        }

        public void setFood_items(List<FoodItemsBean> food_items) {
            this.food_items = food_items;
        }

        public List<UserMetaDataBean> getUser_meta_data() {
            return user_meta_data;
        }

        public void setUser_meta_data(List<UserMetaDataBean> user_meta_data) {
            this.user_meta_data = user_meta_data;
        }

        public static class SeatsInfoBean {
            /**
             * seat_id : 12
             * seat_code : Lima
             */

            private int seat_id;
            private String seat_code;

            public int getSeat_id() {
                return seat_id;
            }

            public void setSeat_id(int seat_id) {
                this.seat_id = seat_id;
            }

            public String getSeat_code() {
                return seat_code;
            }

            public void setSeat_code(String seat_code) {
                this.seat_code = seat_code;
            }
        }





        public static class BoardingPassDataBean {
            /**
             * seat_name : Lima
             * seat_short_code : L
             * traveller : Varun RV
             * aircraft : STLR604
             * date : 16 04 2019
             * time : 1930
             * reportTime : 07.20pm
             * departure : Tuesday 16 April 07.30pm
             * from : Delhi
             * to : Bengaluru
             * from_code : DEL
             * to_code : BLR
             * stellar_club_code : 12
             * flight : S6
             */

            private String seat_name;
            private String seat_short_code;
            private String traveller;
            private String aircraft;
            private String date;
            private String time;
            private String reportTime;
            private String departure;
            private String from;
            private String to;
            private String from_code;
            private String to_code;
            private int stellar_club_code;
            private String flight;

            public String getSeat_name() {
                return seat_name;
            }

            public void setSeat_name(String seat_name) {
                this.seat_name = seat_name;
            }

            public String getSeat_short_code() {
                return seat_short_code;
            }

            public void setSeat_short_code(String seat_short_code) {
                this.seat_short_code = seat_short_code;
            }

            public String getTraveller() {
                return traveller;
            }

            public void setTraveller(String traveller) {
                this.traveller = traveller;
            }

            public String getAircraft() {
                return aircraft;
            }

            public void setAircraft(String aircraft) {
                this.aircraft = aircraft;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getReportTime() {
                return reportTime;
            }

            public void setReportTime(String reportTime) {
                this.reportTime = reportTime;
            }

            public String getDeparture() {
                return departure;
            }

            public void setDeparture(String departure) {
                this.departure = departure;
            }

            public String getFrom() {
                return from;
            }

            public void setFrom(String from) {
                this.from = from;
            }

            public String getTo() {
                return to;
            }

            public void setTo(String to) {
                this.to = to;
            }

            public String getFrom_code() {
                return from_code;
            }

            public void setFrom_code(String from_code) {
                this.from_code = from_code;
            }

            public String getTo_code() {
                return to_code;
            }

            public void setTo_code(String to_code) {
                this.to_code = to_code;
            }

            public int getStellar_club_code() {
                return stellar_club_code;
            }

            public void setStellar_club_code(int stellar_club_code) {
                this.stellar_club_code = stellar_club_code;
            }

            public String getFlight() {
                return flight;
            }

            public void setFlight(String flight) {
                this.flight = flight;
            }
        }

        public static class FoodItemsBean {
            /**
             * id : 6
             * name : Aloo paratha
             * food_type : veg
             * food_type_text : Vegetarian
             */

            private int id;
            private String name;
            private String food_type;
            private String food_type_text;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getFood_type() {
                return food_type;
            }

            public void setFood_type(String food_type) {
                this.food_type = food_type;
            }

            public String getFood_type_text() {
                return food_type_text;
            }

            public void setFood_type_text(String food_type_text) {
                this.food_type_text = food_type_text;
            }
        }

        public static class UserMetaDataBean {
            /**
             * meta_key : food_prefs
             * meta_value : [{"food_category":"veg","days":[]},{"food_category":"non-veg","days":["Monday"]},{"food_category":"continental","days":[]}]
             */

            private String meta_key;
            private List<MetaValueBean> meta_value;

            public String getMeta_key() {
                return meta_key;
            }

            public void setMeta_key(String meta_key) {
                this.meta_key = meta_key;
            }

            public List<MetaValueBean> getMeta_value() {
                return meta_value;
            }

            public void setMeta_value(List<MetaValueBean> meta_value) {
                this.meta_value = meta_value;
            }

            public static class MetaValueBean {
                /**
                 * food_category : veg
                 * days : []
                 */

                private String food_category;
                private List<?> days;

                public String getFood_category() {
                    return food_category;
                }

                public void setFood_category(String food_category) {
                    this.food_category = food_category;
                }

                public List<?> getDays() {
                    return days;
                }

                public void setDays(List<?> days) {
                    this.days = days;
                }
            }
        }
    }
}
