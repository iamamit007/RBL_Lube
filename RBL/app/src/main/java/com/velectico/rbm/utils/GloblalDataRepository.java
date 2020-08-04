package com.velectico.rbm.utils;

import com.velectico.rbm.leave.model.LeaveListModel;

public class GloblalDataRepository {
    private static GloblalDataRepository globalDataService = null;
    private String scheduleId = "";
    private LeaveListModel leaveListModel = null;

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

    private String distribId = "";
    private String taskId = "";
    private String mechId = "";
    private String delalerId = "";

    public static GloblalDataRepository getGlobalDataService() {
        return globalDataService;
    }

    public static void setGlobalDataService(GloblalDataRepository globalDataService) {
        GloblalDataRepository.globalDataService = globalDataService;
    }

    public String getDistribId() {
        return distribId;
    }

    public void setDistribId(String distribId) {
        this.distribId = distribId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getMechId() {
        return mechId;
    }

    public void setMechId(String mechId) {
        this.mechId = mechId;
    }

    public String getDelalerId() {
        return delalerId;
    }

    public void setDelalerId(String delalerId) {
        this.delalerId = delalerId;
    }

    public LeaveListModel getLeaveListModel() {
        return leaveListModel;
    }

    public void setLeaveListModel(LeaveListModel leaveListModel) {
        this.leaveListModel = leaveListModel;
    }
}