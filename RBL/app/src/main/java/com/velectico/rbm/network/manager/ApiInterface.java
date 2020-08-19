package com.velectico.rbm.network.manager;

import com.velectico.rbm.beats.model.AssignTaskRequestParams;
import com.velectico.rbm.beats.model.AssignToListRequestParams;
import com.velectico.rbm.beats.model.AssignToListResponse;
import com.velectico.rbm.beats.model.BeatAllOrderListRequestParams;
import com.velectico.rbm.beats.model.BeatDetailListRequestParams;
import com.velectico.rbm.beats.model.BeatDetailListResponse;
import com.velectico.rbm.beats.model.BeatReportListDetailsResponse;
import com.velectico.rbm.beats.model.BeatReportListRequestParams;
import com.velectico.rbm.beats.model.BeatTaskDetailsListResponse;
import com.velectico.rbm.beats.model.BeatTaskDetailsRequestParams;
import com.velectico.rbm.beats.model.BeatWiseTakListResponse;
import com.velectico.rbm.beats.model.CreateBeatReportRequestParams;
import com.velectico.rbm.beats.model.CreateBeatReportResponse;
import com.velectico.rbm.beats.model.CreateBeatScheduleRequestParams;
import com.velectico.rbm.beats.model.CreateOrderDetailsResponse;
import com.velectico.rbm.beats.model.CreateOrderListRequestParams;
import com.velectico.rbm.beats.model.CreateOrderPRParams;
import com.velectico.rbm.beats.model.CreateOrderResponse;
import com.velectico.rbm.beats.model.DealDistMechListRequestParams;
import com.velectico.rbm.beats.model.DealDistMechListResponse;
import com.velectico.rbm.beats.model.DealListRequestParams;
import com.velectico.rbm.beats.model.DealListResponse;
import com.velectico.rbm.beats.model.DealerDetailsRequestParams;
import com.velectico.rbm.beats.model.DealerDetailsResponse;
import com.velectico.rbm.beats.model.DistListRequestParams;
import com.velectico.rbm.beats.model.DistListResponse;
import com.velectico.rbm.beats.model.GetBeatDeatilsRequestParams;
import com.velectico.rbm.beats.model.LocationByLevelListRequestParams;
import com.velectico.rbm.beats.model.LocationByLevelListResponse;
import com.velectico.rbm.beats.model.OrderDetailsParams;
import com.velectico.rbm.beats.model.OrderHistoryDetailsResponse;
import com.velectico.rbm.beats.model.OrderVSQualityRequestParams;
import com.velectico.rbm.beats.model.OrderVSQualityResponse;
import com.velectico.rbm.beats.model.TaskForListRequestParams;
import com.velectico.rbm.beats.model.TaskForListResponse;
import com.velectico.rbm.complaint.model.ComplaintListRequestParams;
import com.velectico.rbm.complaint.model.ComplaintListResponse;
import com.velectico.rbm.expense.model.BidListResponse;
import com.velectico.rbm.expense.model.CreateExpenseResponse;
import com.velectico.rbm.expense.model.ExpenseCreateRequest;
import com.velectico.rbm.expense.model.ExpenseResponse;
import com.velectico.rbm.leave.model.ApplyLeaveRequest;
import com.velectico.rbm.leave.model.ApplyLeaveResponse;
import com.velectico.rbm.leave.model.ApproveRejectLeaveListRequest;
import com.velectico.rbm.leave.model.LeaveListRequest;
import com.velectico.rbm.leave.model.LeaveListResponse;
import com.velectico.rbm.loginreg.model.ResetPasswordRequestParams;
import com.velectico.rbm.loginreg.model.ResetPasswordResponse;
import com.velectico.rbm.loginreg.model.forgotPasswordRequestParams;
import com.velectico.rbm.loginreg.model.forgotPasswordResponse;
import com.velectico.rbm.menuitems.viewmodel.AttendancResponse;
import com.velectico.rbm.menuitems.viewmodel.AttendanceRequestParams;
import com.velectico.rbm.redeem.model.GetRedeemDetailsRequestParams;
import com.velectico.rbm.redeem.model.GetRedeemDetailsResponse;
import com.velectico.rbm.redeem.model.ReedemRequestParams;
import com.velectico.rbm.redeem.model.ReedemResponse;
import com.velectico.rbm.redeem.model.SendQrRequestParams;
import com.velectico.rbm.redeem.model.SendQrResponse;
import com.velectico.rbm.redeem.view.Redeem;
import com.velectico.rbm.reminder.model.CreateReminderRequestParams;
import com.velectico.rbm.reminder.model.CreateReminderResponse;
import com.velectico.rbm.reminder.model.ReminderListRequestParams;
import com.velectico.rbm.reminder.model.ReminderListResponse;
import com.velectico.rbm.teamlist.model.TeamListRequestParams;
import com.velectico.rbm.teamlist.model.TeamListResponse;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Add_ExpensImage;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Apply_Leave;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Approved_Reject_Leave;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Beat_Report_By_Date;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Complaint_List;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Create_Beat_Report;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Create_Beat_Schedule;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Create_Expense;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Create_Order;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Create_Reminder;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Dealer_Distrib_Task_Worksheet;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Dealer_Dropdown;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Distrib_Dropdown;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.DoAttend;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.ENDPOINTBeat_Task_Details;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.ENDPOINT_GET_TASK_DETAILS_LIST_BY_BEAT_ID;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Edit_Leave;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Forgot_Password;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Expense_List;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Get_AssignTo_By_TaskFor_And_Location;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Get_Beat_By_Level;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Get_Deal_Dist_Mech_List;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Get_Dropdown_Details_byName;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Get_Location_By_Level;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Get_Order_History;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Get_Product_List_By_Cat_Seg;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Get_Reedem_Details;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Get_ReminderList;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Get_Task_For;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Get_Task_dropdown_in_expens;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Leave_List;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Get_Team_List;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Reedem;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Reset_Password;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Save_AssignTask;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Send_QR_Details;

public interface ApiInterface {


//    @GET("{affenpinscher}/images")
//    Call<Product> getProductData(@Path("affenpinscher") String breed);



    @POST(ENDPOINT_GET_TASK_DETAILS_LIST_BY_BEAT_ID)
    Call<BeatWiseTakListResponse> getTaskDetailsByBeat(@Body GetBeatDeatilsRequestParams model);

    @POST(ENDPOINTBeat_Task_Details)
    Call<BeatTaskDetailsListResponse> getScheduleTaskDetailsByBeat(@Body BeatTaskDetailsRequestParams model);

    @POST(Dealer_Distrib_Task_Worksheet)
    Call<DealerDetailsResponse> getDealerDetailsByBeat(@Body DealerDetailsRequestParams model);

    @POST(Get_Order_History)
    Call<OrderHistoryDetailsResponse> getBeatAllOrderHistory(@Body BeatAllOrderListRequestParams model);

    @POST(Get_Product_List_By_Cat_Seg)
    Call<CreateOrderDetailsResponse> getCreateOrderList(@Body CreateOrderListRequestParams model);

    @POST(Beat_Report_By_Date)
    Call<BeatReportListDetailsResponse> getBeatReportList(@Body BeatReportListRequestParams model);

    @POST(Get_Dropdown_Details_byName)
    Call<OrderVSQualityResponse> getOrdervsQualityList(@Body OrderVSQualityRequestParams model);

    @POST(Create_Beat_Report)
    Call<CreateBeatReportResponse> createBeatReport(@Body CreateBeatReportRequestParams model);


    //sec drop down beat create
    @POST(Get_Beat_By_Level)
    Call<BeatDetailListResponse> getBeatDetailList(@Body BeatDetailListRequestParams model);
    //task for secon beat
    @POST(Get_Task_For)
    Call<TaskForListResponse> getTaskForList(@Body TaskForListRequestParams model);

    //3rd drop down
    @POST(Get_Location_By_Level)
    Call<LocationByLevelListResponse> getLocationByLevelList(@Body LocationByLevelListRequestParams model);

    //member list drop down
    @POST(Get_AssignTo_By_TaskFor_And_Location)
    Call<AssignToListResponse> getAssignToList(@Body AssignToListRequestParams model);

    //task
    @POST(Get_Deal_Dist_Mech_List)
    Call<DealDistMechListResponse> getDealDistMechList(@Body DealDistMechListRequestParams model);

    @POST(Create_Beat_Schedule)
    Call<CreateBeatReportResponse> createBeatSchedule(@Body CreateBeatScheduleRequestParams model);

    @POST(Complaint_List)
    Call<ComplaintListResponse> getComplaintList(@Body ComplaintListRequestParams model);

    @POST(Save_AssignTask)
    Call<CreateBeatReportResponse> assignTask(@Body AssignTaskRequestParams model);

    @POST(Create_Order)
    Call<CreateOrderResponse> createOrder(@Body CreateOrderPRParams model);

    @POST(Get_Team_List)
    Call<TeamListResponse> getTeamList(@Body TeamListRequestParams model);


    @POST(Apply_Leave)
    Call<ApplyLeaveResponse> createLeave(@Body ApplyLeaveRequest model);

    @POST(Edit_Leave)
    Call<ApplyLeaveResponse> updateLeave(@Body ApplyLeaveRequest model);

    @POST(Leave_List)
    Call<LeaveListResponse> getLeaveList(@Body LeaveListRequest model);

    @POST(Approved_Reject_Leave)
    Call<ApplyLeaveResponse> accepeRejectLeave(@Body ApproveRejectLeaveListRequest model);

    @POST(DoAttend)
    Call<AttendancResponse> doAttendance(@Body AttendanceRequestParams model);

    @POST(Get_Task_dropdown_in_expens)
    Call<BidListResponse> getBitList(@Body AttendanceRequestParams model);

    @POST(Create_Expense)
    Call<CreateExpenseResponse> createExpense(@Body ExpenseCreateRequest model);

    @Multipart
    @POST(Add_ExpensImage)
    Call<CreateExpenseResponse> uploadpic(
                        @Part("recPhoto1\"; filename=\"pp.png\" ") RequestBody file,
                        @Part("expensId") RequestBody expensId,
                        @Part("userId") RequestBody userId);

    @POST(Distrib_Dropdown)
    Call<DistListResponse> distDropDownList(@Body DistListRequestParams model);

    @POST(Dealer_Dropdown)
    Call<DealListResponse> dealDropDownList(@Body DealListRequestParams model);

    @POST(Get_ReminderList)
    Call<ReminderListResponse> getReminderList(@Body ReminderListRequestParams model);

    @POST(Create_Reminder)
    Call<CreateReminderResponse> addReminder(@Body CreateReminderRequestParams model);

    @POST(Forgot_Password)
    Call<forgotPasswordResponse> forgotPassword(@Body forgotPasswordRequestParams model);

    @POST(Reset_Password)
    Call<ResetPasswordResponse> resetPassword(@Body ResetPasswordRequestParams model);

    @Multipart
    @POST(Add_ExpensImage)
    Call<CreateExpenseResponse> uploadpic(
            @Part MultipartBody.Part filePart,@Part MultipartBody.Part filePart1,@Part MultipartBody.Part filePar2,@Part MultipartBody.Part filePar3);

    @Headers({"Content-Type:text/html; charset=UTF-8","Connection:keep-alive"})
    @Multipart
    @POST(Add_ExpensImage)
    Call<CreateExpenseResponse> uploadpic(

            @PartMap Map<String, RequestBody> map);


    @POST(Expense_List)
    Call<ExpenseResponse> getLeaveList(AttendanceRequestParams model);

    @POST(Expense_List)
    Call<ExpenseResponse> getChuttiList(@Body AttendanceRequestParams model);

    @POST(Send_QR_Details)
    Call<SendQrResponse> sendQrDetails(@Body SendQrRequestParams model);

    @POST(Reedem)
    Call<ReedemResponse> redeemPoints(@Body ReedemRequestParams model);

    @POST(Get_Reedem_Details)
    Call<GetRedeemDetailsResponse> getRedeemList(@Body GetRedeemDetailsRequestParams model);
}
