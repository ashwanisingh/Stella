package com.ns.networking.retrofit;

import android.graphics.Bitmap;
import com.ns.networking.model.LoginResponse;
import com.ns.networking.model.ValidateCustomerResponse;
import com.ns.networking.utils.Constants;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface StellarApiService {

    // login Api
    @POST(Constants.LOGIN_API)
    @FormUrlEncoded
    Call<LoginResponse> doLogin(
            @Field("username") String username,
            @Field("password") String password
    );

    @POST(Constants.VALIDATE_USER_API)
    @FormUrlEncoded
    Call<ValidateCustomerResponse> doValidateCustomer(
            @Field("username") String username
    );
/*
    @GET(Constants.FLIGHT_SCHEDULE_API)
    Call<FlightSchedluesResponse> getFlightSchedules(
            @Query("token") String token,
            @Query("from_city") String fromCity,
            @Query("to_city") String toCity
    );

    @FormUrlEncoded
    @POST(Constants.FLIGHT_SEATS_API)
    Call<FlightSeatsResponse> getFlightSeats(
            @Field("token") String token,
            @Field("flight_id") int flightId,
            @Field("from_city") int fromCity,
            @Field("to_city") int toCity,
            @Field("journey_date") String journeyDate,
            @Field("journey_time") String journeyTime
    );

    @FormUrlEncoded
    @POST(Constants.FLIGHT_SEATS_CONFIRM_API)
    Call<FlightSeatsConfirmDataResponse> confirmFlightSeats(
            @Field("token") String token,
            @Field("flight_id") int flightId,
            @Field("user") String userId,
            @Field("from_city") int fromCity,
            @Field("to_city") int toCity,
            @Field("journey_date") String journeyDate,
            @Field("journey_time") String journeyTime,
            @Field("seats_for_unlock[]") List<Integer> mSeatsUnlock,
            @Field("seats_for_lock[]") List<Integer> mSeatslock

    );

    @FormUrlEncoded
    @POST(Constants.BOOK_SEATS_API)
    Call<BookingConfirmResponse> confirmFlightBooking(
            @Field("token") String token,
            @Field("user") String userId,
            @Field("from_city") int fromCity,
            @Field("to_city") int toCity,
            @Field("journey_date") String journeyDate,
            @Field("journey_time") String journeyTime,
            @Field("pick_address") int pickAddress,
            @Field("drop_address") int dropAddress,
            @Field("flight_id") int flightId,
            @Field("seat_ids[]") List<Integer> mSeatsCodeList,
            @Field("date_of_journey") long mDateOfJourney,
            @Field("guests[]") List<Integer> mGuestId,
            @Field("foods_taken[]") List<String> foodPrefs,
            @Field("kits_taken[]") List<String> kitPrefs,
            @Field("travelling_self") int selfTravelling,
            @Field("need_cab") int cabRequired
//            @Field("guest_prefs[]")ArrayList<GuestPrefsDataRequest> guestPrefs
    );

    @POST(Constants.GUEST_CONFIRM_API)
    Call<GuestConfirmResponse> bookGuestInfo(
            @Body GuestPrefsRequest guestPrefsRequest
    );

    @POST(Constants.GUEST_CONFIRM_API)
    Call<EditGuestConfirmResponse> bookExistingGuestInfo(
            @Body EditGuestPrefsRequest editGuestPrefsRequest
    );

    @POST(Constants.GUEST_CONFIRM_API)
    Call<GuestConfirmResponse> bookNewGuestInfo(
            @Body AddGuestPrefsRequest addGuestPrefsRequest
    );

    @GET
    Call<LoginResponse> getCustomerData(
            @Url String url,
            @Query("token") String token
    );

    @GET
    Call<BookingHistoryResponse> getBookingHistoryResponse(
            @Url String url,
            @Query("token") String token
    );

    @GET(Constants.BOARDING_PASS_API)
    Call<BoardingPassResponse> getBoardingPassResponse(
            @Query("token") String token,
            @Query("list_for") String boardingPass
    );

    @GET(Constants.CITY_LIST_API)
    Call<CityListResponse> getCityList(
            @Query("token") String token
    );

    @FormUrlEncoded
    @POST(Constants.ADD_ADDRESS_API)
    Call<AddAddressResponse> addNewAddress(
            @Field("token") String token,
            @Field("user") String userId,
            @Field("city") String cityId,
            @Field("address") String address,
            @Field("address_for") String addressFor,
            @Field("address_tag") String addressTag
    );

    @FormUrlEncoded
    @POST(Constants.ADD_GUEST_API)
    Call<AddGuestResponse> addNewGuest(
            @Field("token") String token,
            @Field("user") String userId,
            @Field("name") String name,
            @Field("phone") String phone,
            @Field("nick_name") String nickName,
            @Field("relationship") String relationship,
            @Field("email") String email
    );

    @FormUrlEncoded
    @POST(Constants.UPDATE_PREFERENCES_API)
    Call<UpdatePreferencesResponse> updatePreferences(
            @Field("token") String token,
            @Field("user") String userId,
            @Field("food_prefs[]") List<String> foodPrefs,
            @Field("kit_prefs[]") List<String> kitPrefs
    );*/
}
