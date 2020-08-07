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
import com.velectico.rbm.beats.model.DealerDetailsRequestParams;
import com.velectico.rbm.beats.model.DealerDetailsResponse;
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
import com.velectico.rbm.leave.model.ApplyLeaveRequest;
import com.velectico.rbm.leave.model.ApplyLeaveResponse;
import com.velectico.rbm.leave.model.ApproveRejectLeaveListRequest;
import com.velectico.rbm.leave.model.LeaveListRequest;
import com.velectico.rbm.leave.model.LeaveListResponse;
import com.velectico.rbm.teamlist.model.TeamListRequestParams;
import com.velectico.rbm.teamlist.model.TeamListResponse;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;

import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Apply_Leave;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Approved_Reject_Leave;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Beat_Report_By_Date;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Complaint_List;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Create_Beat_Report;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Create_Beat_Schedule;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Create_Order;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Dealer_Distrib_Task_Worksheet;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.ENDPOINTBeat_Task_Details;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.ENDPOINT_GET_TASK_DETAILS_LIST_BY_BEAT_ID;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Edit_Leave;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Get_AssignTo_By_TaskFor_And_Location;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Get_Beat_By_Level;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Get_Deal_Dist_Mech_List;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Get_Dropdown_Details_byName;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Get_Location_By_Level;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Get_Order_History;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Get_Product_List_By_Cat_Seg;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Get_Task_For;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Leave_List;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Get_Team_List;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Save_AssignTask;

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






}
