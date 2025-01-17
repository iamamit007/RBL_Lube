package com.velectico.rbm.network.apiconstants

/**
 * Created by mymacbookpro on 2020-04-30
 * TODO: Add a class header comment!
 */
object ConstantAPI {
    val prod:String get() = ""

    val test:String get() = "https://reqres.in"

    val dev:String get() = "https://velectico.top"
}

///////////////////////////////////////////////////////////////////////

const val CONNECT_TIME_OUT:Long = 1
const val READ_TIME_OUT:Long = 15
const val WRITE_TIME_OUT:Long = 15

const val ERROR_CODE_UNKNOWN_HOST = 604
const val ERROR_CODE_TIME_OUT = 408
const val ERROR_CODE_OTHER = 600

///////////////////////////////////////////////////////////////////////

const val READ_ALL_RESOURCES_DATA = "ReadAllResourcesData"
const val READ_ALL_USERS_DATA = "ReadAllUsersData"
const val USER_LOGIN = "UserLogin"
const val PRODUCT_LIST = "ProductList"
const val EXPENSE_LIST = "ExpenseList"
const val MASTER_DATA_LIST = "MasterDataList"
const val EXPENSE_CREATE_EDIT = "CreateEditExpense"
const val EXPENSE_DELETE = "DeleteExpense"

const val LEAVE_REASON = "LeaveReason"
const val LEAVE_APPLY = "ApplyLeave"
const val LEAVE_EDIT = "EditLeave"
const val LEAVE_DELETE = "DeleteLeave"
const val LEAVE_LIST = "ListLeave"



const val REQ_HEADER_TYPE_KEY = "Type"
const val REQ_HEADER_TYPE_VAL = "App"
const val REQ_HEADER_DEVICE_ID_KEY = "Userdeviceid"
const val REQ_HEADER_DEVICE_ID_VAL = "569966666"
const val REQ_HEADER_DEVICE_TYPE_KEY = "Devicetype"
const val REQ_HEADER_DEVICE_TYPE_VAL = "Android"
const val USER_LOGIN_MOBILE = "userMobile"
const val USER_LOGIN_PASSWORD = "userPassword"

const val USER_ID = "userId"
const val USER_ROLE = "userRole"
const val DROP_DOWN_NAME = "DM_Dropdown_Name"
//create expense params
const val BEAT_TASK_ID = "beatTaskId"
const val EXP_HEAD_ID = "Exp_Head_Id"
const val MIS_EXPENSE_AMOUNT = "misExpenseAmt"
const val APPLIED_ON_DATE = "appliedOnDate"
const val APPLIED_BY_USER_ID = "applieedByUserId"
const val FILE_TO_UPLOAD = "recPhoto"
const val EXPENSE_ID = "expenseId"
//Leave params
const val LEAVE_ID = "leaveId"
const val LEAVE_FROM = "leaveFromDate"
const val LEAVE_TO = "leaveToDate"
const val LEAVE_REASON_ID = "leaveReasonId"
const val LEAVE_REASON_OTHER = "leaveReasonOther"


const val GOOGLE_BASE_URL = "https://www.google.com"
const val ENDPOINT_GET_RATES = "/pranaycare/server/getRate.php"
const val ENDPOINT_GET_PHOTOS = "photos"
const val ENDPOINT_GET_RESOURCES = "/api/unknown"
const val ENDPOINT_GET_ALL_USERS = "/api/users"

const val APP_API_NAME = "/RBM-Lubricants/API/"

const val ENDPOINT_USER_LOGIN   = APP_API_NAME+"Login"
const val ENDPOINT_PRODUCT_LIST = APP_API_NAME+"ProductDetails"
const val ENDPOINT_BEAT_MASTER_DATA   = APP_API_NAME+"GetBeatMasterData"
const val ENDPOINT_EXPENSE_LIST  = APP_API_NAME+"Expense_List"
const val ENDPOINT_EXPENSE_CREATE_EDIT  = APP_API_NAME+"Create_Expense"
const val ENDPOINT_DELETE_EXPENSE  = APP_API_NAME+"Delete_Expense"
const val ENDPOINT_MASTER_DATA_LIST  = APP_API_NAME+"Get_Dropdown_Details_byName"

//LEAVE ENDPOINTS

const val ENDPOINT_LEAVE_REASON = APP_API_NAME+"Get_Leave_Reason"
const val ENDPOINT_LEAVE_APPLY = APP_API_NAME+"Apply_Leave"
const val ENDPOINT_LEAVE_EDIT = APP_API_NAME+"Edit_Leave"
const val ENDPOINT_LEAVE_DELETE = APP_API_NAME+"Delete_Leave"
const val ENDPOINT_LEAVE_LIST = APP_API_NAME+"Leave_List"






