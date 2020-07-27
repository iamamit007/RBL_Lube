package com.velectico.rbm.network.manager;

import com.velectico.rbm.beats.model.BeatAllOrderListRequestParams;
import com.velectico.rbm.beats.model.BeatTaskDetailsListResponse;
import com.velectico.rbm.beats.model.BeatTaskDetailsRequestParams;
import com.velectico.rbm.beats.model.BeatWiseTakListResponse;
import com.velectico.rbm.beats.model.DealerDetailsRequestParams;
import com.velectico.rbm.beats.model.DealerDetailsResponse;
import com.velectico.rbm.beats.model.GetBeatDeatilsRequestParams;
import com.velectico.rbm.beats.model.OrderHistoryDetailsResponse;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;

import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Dealer_Distrib_Task_Worksheet;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.ENDPOINTBeat_Task_Details;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.ENDPOINT_GET_TASK_DETAILS_LIST_BY_BEAT_ID;
import static com.velectico.rbm.network.apiconstants.ConstantAPIKt.Get_Order_History;

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


}
