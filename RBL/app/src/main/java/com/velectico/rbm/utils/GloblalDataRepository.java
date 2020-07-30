package com.velectico.rbm.utils;

public class GloblalDataRepository {
    private static GloblalDataRepository globalDataService = null;
    private String scheduleId = "";

    public static GloblalDataRepository getInstance() {
        if (globalDataService == null)
            globalDataService = new GloblalDataRepository();

        return globalDataService;
    }

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }
}
