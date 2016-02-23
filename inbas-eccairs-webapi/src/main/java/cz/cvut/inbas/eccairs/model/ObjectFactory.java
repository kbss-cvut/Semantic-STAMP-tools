
package cz.cvut.inbas.eccairs.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the eccairs5webapi package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _UnsignedLong_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "unsignedLong");
    private final static QName _UnsignedByte_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "unsignedByte");
    private final static QName _UnsignedInt_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "unsignedInt");
    private final static QName _Char_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "char");
    private final static QName _Short_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "short");
    private final static QName _Guid_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "guid");
    private final static QName _UnsignedShort_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "unsignedShort");
    private final static QName _ResultReturnCode_QNAME = new QName("http://schemas.datacontract.org/2004/07/ECCAIRS5.Repository.Web.API", "ResultReturnCode");
    private final static QName _Decimal_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "decimal");
    private final static QName _Boolean_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "boolean");
    private final static QName _Duration_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "duration");
    private final static QName _Base64Binary_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "base64Binary");
    private final static QName _Int_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "int");
    private final static QName _Long_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "long");
    private final static QName _AnyURI_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "anyURI");
    private final static QName _Float_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "float");
    private final static QName _EwaResult_QNAME = new QName("http://schemas.datacontract.org/2004/07/ECCAIRS5.Repository.Web.API", "ewaResult");
    private final static QName _DateTime_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "dateTime");
    private final static QName _Byte_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "byte");
    private final static QName _Double_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "double");
    private final static QName _QName_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "QName");
    private final static QName _AnyType_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "anyType");
    private final static QName _String_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "string");
    private final static QName _GetImpersonatedUserCredentialsResponseGetImpersonatedUserCredentialsResult_QNAME = new QName("http://tempuri.org/", "GetImpersonatedUserCredentialsResult");
    private final static QName _CreateUserResponseCreateUserResult_QNAME = new QName("http://tempuri.org/", "CreateUserResult");
    private final static QName _UserManagerLoginByInfoLoginInfo_QNAME = new QName("http://tempuri.org/", "loginInfo");
    private final static QName _GetQueryObjectUserToken_QNAME = new QName("http://tempuri.org/", "userToken");
    private final static QName _GetQueryObjectLibraryName_QNAME = new QName("http://tempuri.org/", "libraryName");
    private final static QName _GetQueryObjectQueryName_QNAME = new QName("http://tempuri.org/", "queryName");
    private final static QName _GetOccurrenceByKeyOccurrenceKey_QNAME = new QName("http://tempuri.org/", "occurrenceKey");
    private final static QName _GetAttachmentsDetailsByKeyResponseGetAttachmentsDetailsByKeyResult_QNAME = new QName("http://tempuri.org/", "GetAttachmentsDetailsByKeyResult");
    private final static QName _ExecuteQueryObjectResponseExecuteQueryObjectResult_QNAME = new QName("http://tempuri.org/", "ExecuteQueryObjectResult");
    private final static QName _SaveUserResponseSaveUserResult_QNAME = new QName("http://tempuri.org/", "SaveUserResult");
    private final static QName _ExecuteQueryObjectQueryObject_QNAME = new QName("http://tempuri.org/", "queryObject");
    private final static QName _GetQueryByExampleObjectResponseGetQueryByExampleObjectResult_QNAME = new QName("http://tempuri.org/", "GetQueryByExampleObjectResult");
    private final static QName _GetOccurrenceByKeyResponseGetOccurrenceByKeyResult_QNAME = new QName("http://tempuri.org/", "GetOccurrenceByKeyResult");
    private final static QName _UserManagerLoginResponseUserManagerLoginResult_QNAME = new QName("http://tempuri.org/", "UserManagerLoginResult");
    private final static QName _AttachmentExistsResponseAttachmentExistsResult_QNAME = new QName("http://tempuri.org/", "AttachmentExistsResult");
    private final static QName _GetViewsEccairsNumber_QNAME = new QName("http://tempuri.org/", "eccairsNumber");
    private final static QName _GetQueriesByExampleResponseGetQueriesByExampleResult_QNAME = new QName("http://tempuri.org/", "GetQueriesByExampleResult");
    private final static QName _GetExecutedQueryOperationID_QNAME = new QName("http://tempuri.org/", "operationID");
    private final static QName _GetAttributesResponseGetAttributesResult_QNAME = new QName("http://tempuri.org/", "GetAttributesResult");
    private final static QName _GetQueriesResponseGetQueriesResult_QNAME = new QName("http://tempuri.org/", "GetQueriesResult");
    private final static QName _RefreshOccurrenceDataByKeyResponseRefreshOccurrenceDataByKeyResult_QNAME = new QName("http://tempuri.org/", "RefreshOccurrenceDataByKeyResult");
    private final static QName _RefreshOccurrenceResponseRefreshOccurrenceResult_QNAME = new QName("http://tempuri.org/", "RefreshOccurrenceResult");
    private final static QName _GetViewsResponseGetViewsResult_QNAME = new QName("http://tempuri.org/", "GetViewsResult");
    private final static QName _GetUsersUserManagerToken_QNAME = new QName("http://tempuri.org/", "userManagerToken");
    private final static QName _LogoutResponseLogoutResult_QNAME = new QName("http://tempuri.org/", "LogoutResult");
    private final static QName _GetLexiconTextResponseGetLexiconTextResult_QNAME = new QName("http://tempuri.org/", "GetLexiconTextResult");
    private final static QName _LoginPassword_QNAME = new QName("http://tempuri.org/", "password");
    private final static QName _LoginLanguage_QNAME = new QName("http://tempuri.org/", "language");
    private final static QName _LoginUsername_QNAME = new QName("http://tempuri.org/", "username");
    private final static QName _LoginRepository_QNAME = new QName("http://tempuri.org/", "repository");
    private final static QName _GetAllSubRepositoriesResponseGetAllSubRepositoriesResult_QNAME = new QName("http://tempuri.org/", "GetAllSubRepositoriesResult");
    private final static QName _GetQueryByExampleDataResponseGetQueryByExampleDataResult_QNAME = new QName("http://tempuri.org/", "GetQueryByExampleDataResult");
    private final static QName _FormatOccurrenceWithTemplateTemplateName_QNAME = new QName("http://tempuri.org/", "templateName");
    private final static QName _AttachmentExistsAttachmentKey_QNAME = new QName("http://tempuri.org/", "attachmentKey");
    private final static QName _AboutResponseAboutResult_QNAME = new QName("http://tempuri.org/", "AboutResult");
    private final static QName _GetOccurrencePrintingTemplatesResponseGetOccurrencePrintingTemplatesResult_QNAME = new QName("http://tempuri.org/", "GetOccurrencePrintingTemplatesResult");
    private final static QName _ExecuteQueryAsTableResponseExecuteQueryAsTableResult_QNAME = new QName("http://tempuri.org/", "ExecuteQueryAsTableResult");
    private final static QName _ExecuteQueryCountResponseExecuteQueryCountResult_QNAME = new QName("http://tempuri.org/", "ExecuteQueryCountResult");
    private final static QName _LoginByInfoResponseLoginByInfoResult_QNAME = new QName("http://tempuri.org/", "LoginByInfoResult");
    private final static QName _ExecuteQueryObjectCountRawResponseExecuteQueryObjectCountRawResult_QNAME = new QName("http://tempuri.org/", "ExecuteQueryObjectCountRawResult");
    private final static QName _GetQueryResultOrder_QNAME = new QName("http://tempuri.org/", "order");
    private final static QName _DeleteUserResponseDeleteUserResult_QNAME = new QName("http://tempuri.org/", "DeleteUserResult");
    private final static QName _GetExecutedQueryObjectResponseGetExecutedQueryObjectResult_QNAME = new QName("http://tempuri.org/", "GetExecutedQueryObjectResult");
    private final static QName _GetViewImageViewName_QNAME = new QName("http://tempuri.org/", "viewName");
    private final static QName _GetEntitiesResponseGetEntitiesResult_QNAME = new QName("http://tempuri.org/", "GetEntitiesResult");
    private final static QName _GetAttributeValueValueData_QNAME = new QName("http://tempuri.org/", "valueData");
    private final static QName _GetOccurrenceDataByKeyResponseGetOccurrenceDataByKeyResult_QNAME = new QName("http://tempuri.org/", "GetOccurrenceDataByKeyResult");
    private final static QName _ReleaseQueryResultResponseReleaseQueryResultResult_QNAME = new QName("http://tempuri.org/", "ReleaseQueryResultResult");
    private final static QName _GetQueryObjectResponseGetQueryObjectResult_QNAME = new QName("http://tempuri.org/", "GetQueryObjectResult");
    private final static QName _SaveUserRawResponseSaveUserRawResult_QNAME = new QName("http://tempuri.org/", "SaveUserRawResult");
    private final static QName _GetRepositoriesResponseGetRepositoriesResult_QNAME = new QName("http://tempuri.org/", "GetRepositoriesResult");
    private final static QName _SaveUserInfoResponseSaveUserInfoResult_QNAME = new QName("http://tempuri.org/", "SaveUserInfoResult");
    private final static QName _GetOccurrenceResponseGetOccurrenceResult_QNAME = new QName("http://tempuri.org/", "GetOccurrenceResult");
    private final static QName _GetLibrariesResponseGetLibrariesResult_QNAME = new QName("http://tempuri.org/", "GetLibrariesResult");
    private final static QName _GetAttributeResponseGetAttributeResult_QNAME = new QName("http://tempuri.org/", "GetAttributeResult");
    private final static QName _GetUserInfoResponseGetUserInfoResult_QNAME = new QName("http://tempuri.org/", "GetUserInfoResult");
    private final static QName _GetAllSubRepositoriesRootRepository_QNAME = new QName("http://tempuri.org/", "rootRepository");
    private final static QName _GetValuePathResponseGetValuePathResult_QNAME = new QName("http://tempuri.org/", "GetValuePathResult");
    private final static QName _GetOccurrenceDataResponseGetOccurrenceDataResult_QNAME = new QName("http://tempuri.org/", "GetOccurrenceDataResult");
    private final static QName _EwaResultErrorDetails_QNAME = new QName("http://schemas.datacontract.org/2004/07/ECCAIRS5.Repository.Web.API", "ErrorDetails");
    private final static QName _EwaResultData_QNAME = new QName("http://schemas.datacontract.org/2004/07/ECCAIRS5.Repository.Web.API", "Data");
    private final static QName _EwaResultUserToken_QNAME = new QName("http://schemas.datacontract.org/2004/07/ECCAIRS5.Repository.Web.API", "UserToken");
    private final static QName _ExecuteQueryResponseExecuteQueryResult_QNAME = new QName("http://tempuri.org/", "ExecuteQueryResult");
    private final static QName _UserManagerLoginByInfoResponseUserManagerLoginByInfoResult_QNAME = new QName("http://tempuri.org/", "UserManagerLoginByInfoResult");
    private final static QName _ExecuteQueryObjectAsTableRawResponseExecuteQueryObjectAsTableRawResult_QNAME = new QName("http://tempuri.org/", "ExecuteQueryObjectAsTableRawResult");
    private final static QName _GetQueryDataResponseGetQueryDataResult_QNAME = new QName("http://tempuri.org/", "GetQueryDataResult");
    private final static QName _ExecuteQueryObjectRawResponseExecuteQueryObjectRawResult_QNAME = new QName("http://tempuri.org/", "ExecuteQueryObjectRawResult");
    private final static QName _GetUnitsResponseGetUnitsResult_QNAME = new QName("http://tempuri.org/", "GetUnitsResult");
    private final static QName _GetEntityResponseGetEntityResult_QNAME = new QName("http://tempuri.org/", "GetEntityResult");
    private final static QName _GetViewsByKeyResponseGetViewsByKeyResult_QNAME = new QName("http://tempuri.org/", "GetViewsByKeyResult");
    private final static QName _GetQueryResultResponseGetQueryResultResult_QNAME = new QName("http://tempuri.org/", "GetQueryResultResult");
    private final static QName _GetQueryResponseGetQueryResult_QNAME = new QName("http://tempuri.org/", "GetQueryResult");
    private final static QName _SetAttributeUnitIDResponseSetAttributeUnitIDResult_QNAME = new QName("http://tempuri.org/", "SetAttributeUnitIDResult");
    private final static QName _ExecuteQueryObjectAsTableResponseExecuteQueryObjectAsTableResult_QNAME = new QName("http://tempuri.org/", "ExecuteQueryObjectAsTableResult");
    private final static QName _GetAttachmentsDetailsResponseGetAttachmentsDetailsResult_QNAME = new QName("http://tempuri.org/", "GetAttachmentsDetailsResult");
    private final static QName _SaveUserUser_QNAME = new QName("http://tempuri.org/", "user");
    private final static QName _GetValuesResponseGetValuesResult_QNAME = new QName("http://tempuri.org/", "GetValuesResult");
    private final static QName _GetUsersResponseGetUsersResult_QNAME = new QName("http://tempuri.org/", "GetUsersResult");
    private final static QName _GetUserResponseGetUserResult_QNAME = new QName("http://tempuri.org/", "GetUserResult");
    private final static QName _ExecuteQueryObjectCountResponseExecuteQueryObjectCountResult_QNAME = new QName("http://tempuri.org/", "ExecuteQueryObjectCountResult");
    private final static QName _IsValidTokenResponseIsValidTokenResult_QNAME = new QName("http://tempuri.org/", "IsValidTokenResult");
    private final static QName _IsValidQueryResultResponseIsValidQueryResultResult_QNAME = new QName("http://tempuri.org/", "IsValidQueryResultResult");
    private final static QName _LoginResponseLoginResult_QNAME = new QName("http://tempuri.org/", "LoginResult");
    private final static QName _GetAttributeValueResponseGetAttributeValueResult_QNAME = new QName("http://tempuri.org/", "GetAttributeValueResult");
    private final static QName _GetValuesFilter_QNAME = new QName("http://tempuri.org/", "filter");
    private final static QName _SaveUserInfoRawResponseSaveUserInfoRawResult_QNAME = new QName("http://tempuri.org/", "SaveUserInfoRawResult");
    private final static QName _GetExecutedQueryResponseGetExecutedQueryResult_QNAME = new QName("http://tempuri.org/", "GetExecutedQueryResult");
    private final static QName _SaveUserInfoUserInfo_QNAME = new QName("http://tempuri.org/", "userInfo");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: eccairs5webapi
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link FormatOccurrenceWithTemplateByKeyResponse }
     * 
     */
    public FormatOccurrenceWithTemplateByKeyResponse createFormatOccurrenceWithTemplateByKeyResponse() {
        return new FormatOccurrenceWithTemplateByKeyResponse();
    }

    /**
     * Create an instance of {@link GetQueryByExampleDataResponse }
     * 
     */
    public GetQueryByExampleDataResponse createGetQueryByExampleDataResponse() {
        return new GetQueryByExampleDataResponse();
    }

    /**
     * Create an instance of {@link EwaResult }
     * 
     */
    public EwaResult createEwaResult() {
        return new EwaResult();
    }

    /**
     * Create an instance of {@link ExecuteQueryResponse }
     * 
     */
    public ExecuteQueryResponse createExecuteQueryResponse() {
        return new ExecuteQueryResponse();
    }

    /**
     * Create an instance of {@link ExecuteQueryCount }
     * 
     */
    public ExecuteQueryCount createExecuteQueryCount() {
        return new ExecuteQueryCount();
    }

    /**
     * Create an instance of {@link GetQueriesByExampleResponse }
     * 
     */
    public GetQueriesByExampleResponse createGetQueriesByExampleResponse() {
        return new GetQueriesByExampleResponse();
    }

    /**
     * Create an instance of {@link GetViewsByKeyResponse }
     * 
     */
    public GetViewsByKeyResponse createGetViewsByKeyResponse() {
        return new GetViewsByKeyResponse();
    }

    /**
     * Create an instance of {@link GetAttribute }
     * 
     */
    public GetAttribute createGetAttribute() {
        return new GetAttribute();
    }

    /**
     * Create an instance of {@link ExecuteQueryObject }
     * 
     */
    public ExecuteQueryObject createExecuteQueryObject() {
        return new ExecuteQueryObject();
    }

    /**
     * Create an instance of {@link GetViewImageResponse }
     * 
     */
    public GetViewImageResponse createGetViewImageResponse() {
        return new GetViewImageResponse();
    }

    /**
     * Create an instance of {@link ExecuteQueryObjectAsTableResponse }
     * 
     */
    public ExecuteQueryObjectAsTableResponse createExecuteQueryObjectAsTableResponse() {
        return new ExecuteQueryObjectAsTableResponse();
    }

    /**
     * Create an instance of {@link IsValidQueryResultResponse }
     * 
     */
    public IsValidQueryResultResponse createIsValidQueryResultResponse() {
        return new IsValidQueryResultResponse();
    }

    /**
     * Create an instance of {@link GetAttributesResponse }
     * 
     */
    public GetAttributesResponse createGetAttributesResponse() {
        return new GetAttributesResponse();
    }

    /**
     * Create an instance of {@link GetOccurrenceData }
     * 
     */
    public GetOccurrenceData createGetOccurrenceData() {
        return new GetOccurrenceData();
    }

    /**
     * Create an instance of {@link GetAttachmentsDetailsResponse }
     * 
     */
    public GetAttachmentsDetailsResponse createGetAttachmentsDetailsResponse() {
        return new GetAttachmentsDetailsResponse();
    }

    /**
     * Create an instance of {@link GetQueryObject }
     * 
     */
    public GetQueryObject createGetQueryObject() {
        return new GetQueryObject();
    }

    /**
     * Create an instance of {@link ReleaseQueryResult }
     * 
     */
    public ReleaseQueryResult createReleaseQueryResult() {
        return new ReleaseQueryResult();
    }

    /**
     * Create an instance of {@link RefreshOccurrence }
     * 
     */
    public RefreshOccurrence createRefreshOccurrence() {
        return new RefreshOccurrence();
    }

    /**
     * Create an instance of {@link GetAttachmentsDetails }
     * 
     */
    public GetAttachmentsDetails createGetAttachmentsDetails() {
        return new GetAttachmentsDetails();
    }

    /**
     * Create an instance of {@link GetQueryObjectResponse }
     * 
     */
    public GetQueryObjectResponse createGetQueryObjectResponse() {
        return new GetQueryObjectResponse();
    }

    /**
     * Create an instance of {@link ExecuteQueryObjectResponse }
     * 
     */
    public ExecuteQueryObjectResponse createExecuteQueryObjectResponse() {
        return new ExecuteQueryObjectResponse();
    }

    /**
     * Create an instance of {@link CreateUserResponse }
     * 
     */
    public CreateUserResponse createCreateUserResponse() {
        return new CreateUserResponse();
    }

    /**
     * Create an instance of {@link FormatOccurrenceWithTemplate }
     * 
     */
    public FormatOccurrenceWithTemplate createFormatOccurrenceWithTemplate() {
        return new FormatOccurrenceWithTemplate();
    }

    /**
     * Create an instance of {@link GetOccurrenceAsPDFByKeyResponse }
     * 
     */
    public GetOccurrenceAsPDFByKeyResponse createGetOccurrenceAsPDFByKeyResponse() {
        return new GetOccurrenceAsPDFByKeyResponse();
    }

    /**
     * Create an instance of {@link GetViewImage }
     * 
     */
    public GetViewImage createGetViewImage() {
        return new GetViewImage();
    }

    /**
     * Create an instance of {@link GetAttributes }
     * 
     */
    public GetAttributes createGetAttributes() {
        return new GetAttributes();
    }

    /**
     * Create an instance of {@link UserManagerLoginByInfo }
     * 
     */
    public UserManagerLoginByInfo createUserManagerLoginByInfo() {
        return new UserManagerLoginByInfo();
    }

    /**
     * Create an instance of {@link GetViewsResponse }
     * 
     */
    public GetViewsResponse createGetViewsResponse() {
        return new GetViewsResponse();
    }

    /**
     * Create an instance of {@link Login }
     * 
     */
    public Login createLogin() {
        return new Login();
    }

    /**
     * Create an instance of {@link GetOccurrenceDataByKey }
     * 
     */
    public GetOccurrenceDataByKey createGetOccurrenceDataByKey() {
        return new GetOccurrenceDataByKey();
    }

    /**
     * Create an instance of {@link GetAttachment }
     * 
     */
    public GetAttachment createGetAttachment() {
        return new GetAttachment();
    }

    /**
     * Create an instance of {@link GetViewsByKey }
     * 
     */
    public GetViewsByKey createGetViewsByKey() {
        return new GetViewsByKey();
    }

    /**
     * Create an instance of {@link GetOccurrenceByKeyResponse }
     * 
     */
    public GetOccurrenceByKeyResponse createGetOccurrenceByKeyResponse() {
        return new GetOccurrenceByKeyResponse();
    }

    /**
     * Create an instance of {@link RefreshOccurrenceResponse }
     * 
     */
    public RefreshOccurrenceResponse createRefreshOccurrenceResponse() {
        return new RefreshOccurrenceResponse();
    }

    /**
     * Create an instance of {@link GetUserInfo }
     * 
     */
    public GetUserInfo createGetUserInfo() {
        return new GetUserInfo();
    }

    /**
     * Create an instance of {@link GetValuePath }
     * 
     */
    public GetValuePath createGetValuePath() {
        return new GetValuePath();
    }

    /**
     * Create an instance of {@link GetAttributeValueResponse }
     * 
     */
    public GetAttributeValueResponse createGetAttributeValueResponse() {
        return new GetAttributeValueResponse();
    }

    /**
     * Create an instance of {@link GetQueriesByExample }
     * 
     */
    public GetQueriesByExample createGetQueriesByExample() {
        return new GetQueriesByExample();
    }

    /**
     * Create an instance of {@link AttachmentExistsResponse }
     * 
     */
    public AttachmentExistsResponse createAttachmentExistsResponse() {
        return new AttachmentExistsResponse();
    }

    /**
     * Create an instance of {@link ExecuteQueryObjectCountResponse }
     * 
     */
    public ExecuteQueryObjectCountResponse createExecuteQueryObjectCountResponse() {
        return new ExecuteQueryObjectCountResponse();
    }

    /**
     * Create an instance of {@link GetOccurrenceAsPDFResponse }
     * 
     */
    public GetOccurrenceAsPDFResponse createGetOccurrenceAsPDFResponse() {
        return new GetOccurrenceAsPDFResponse();
    }

    /**
     * Create an instance of {@link GetRepositories }
     * 
     */
    public GetRepositories createGetRepositories() {
        return new GetRepositories();
    }

    /**
     * Create an instance of {@link FormatOccurrenceWithTemplateByKey }
     * 
     */
    public FormatOccurrenceWithTemplateByKey createFormatOccurrenceWithTemplateByKey() {
        return new FormatOccurrenceWithTemplateByKey();
    }

    /**
     * Create an instance of {@link GetOccurrenceResponse }
     * 
     */
    public GetOccurrenceResponse createGetOccurrenceResponse() {
        return new GetOccurrenceResponse();
    }

    /**
     * Create an instance of {@link GetEntitiesResponse }
     * 
     */
    public GetEntitiesResponse createGetEntitiesResponse() {
        return new GetEntitiesResponse();
    }

    /**
     * Create an instance of {@link ExecuteQueryObjectCountRaw }
     * 
     */
    public ExecuteQueryObjectCountRaw createExecuteQueryObjectCountRaw() {
        return new ExecuteQueryObjectCountRaw();
    }

    /**
     * Create an instance of {@link GetOccurrenceByKey }
     * 
     */
    public GetOccurrenceByKey createGetOccurrenceByKey() {
        return new GetOccurrenceByKey();
    }

    /**
     * Create an instance of {@link ExecuteQueryObjectRaw }
     * 
     */
    public ExecuteQueryObjectRaw createExecuteQueryObjectRaw() {
        return new ExecuteQueryObjectRaw();
    }

    /**
     * Create an instance of {@link Logout }
     * 
     */
    public Logout createLogout() {
        return new Logout();
    }

    /**
     * Create an instance of {@link SaveUser }
     * 
     */
    public SaveUser createSaveUser() {
        return new SaveUser();
    }

    /**
     * Create an instance of {@link DeleteUserResponse }
     * 
     */
    public DeleteUserResponse createDeleteUserResponse() {
        return new DeleteUserResponse();
    }

    /**
     * Create an instance of {@link AboutResponse }
     * 
     */
    public AboutResponse createAboutResponse() {
        return new AboutResponse();
    }

    /**
     * Create an instance of {@link GetOccurrenceAsPDFByKey }
     * 
     */
    public GetOccurrenceAsPDFByKey createGetOccurrenceAsPDFByKey() {
        return new GetOccurrenceAsPDFByKey();
    }

    /**
     * Create an instance of {@link IsValidTokenResponse }
     * 
     */
    public IsValidTokenResponse createIsValidTokenResponse() {
        return new IsValidTokenResponse();
    }

    /**
     * Create an instance of {@link LogoutResponse }
     * 
     */
    public LogoutResponse createLogoutResponse() {
        return new LogoutResponse();
    }

    /**
     * Create an instance of {@link AttachmentExists }
     * 
     */
    public AttachmentExists createAttachmentExists() {
        return new AttachmentExists();
    }

    /**
     * Create an instance of {@link CreateUser }
     * 
     */
    public CreateUser createCreateUser() {
        return new CreateUser();
    }

    /**
     * Create an instance of {@link ExecuteQueryObjectCount }
     * 
     */
    public ExecuteQueryObjectCount createExecuteQueryObjectCount() {
        return new ExecuteQueryObjectCount();
    }

    /**
     * Create an instance of {@link GetUsers }
     * 
     */
    public GetUsers createGetUsers() {
        return new GetUsers();
    }

    /**
     * Create an instance of {@link SaveUserInfoRawResponse }
     * 
     */
    public SaveUserInfoRawResponse createSaveUserInfoRawResponse() {
        return new SaveUserInfoRawResponse();
    }

    /**
     * Create an instance of {@link GetImpersonatedUserCredentialsResponse }
     * 
     */
    public GetImpersonatedUserCredentialsResponse createGetImpersonatedUserCredentialsResponse() {
        return new GetImpersonatedUserCredentialsResponse();
    }

    /**
     * Create an instance of {@link GetOccurrenceDataByKeyResponse }
     * 
     */
    public GetOccurrenceDataByKeyResponse createGetOccurrenceDataByKeyResponse() {
        return new GetOccurrenceDataByKeyResponse();
    }

    /**
     * Create an instance of {@link ExecuteQueryObjectCountRawResponse }
     * 
     */
    public ExecuteQueryObjectCountRawResponse createExecuteQueryObjectCountRawResponse() {
        return new ExecuteQueryObjectCountRawResponse();
    }

    /**
     * Create an instance of {@link GetLibrariesResponse }
     * 
     */
    public GetLibrariesResponse createGetLibrariesResponse() {
        return new GetLibrariesResponse();
    }

    /**
     * Create an instance of {@link SaveUserInfoRaw }
     * 
     */
    public SaveUserInfoRaw createSaveUserInfoRaw() {
        return new SaveUserInfoRaw();
    }

    /**
     * Create an instance of {@link GetRepositoriesResponse }
     * 
     */
    public GetRepositoriesResponse createGetRepositoriesResponse() {
        return new GetRepositoriesResponse();
    }

    /**
     * Create an instance of {@link LoginByInfoResponse }
     * 
     */
    public LoginByInfoResponse createLoginByInfoResponse() {
        return new LoginByInfoResponse();
    }

    /**
     * Create an instance of {@link RefreshOccurrenceDataByKey }
     * 
     */
    public RefreshOccurrenceDataByKey createRefreshOccurrenceDataByKey() {
        return new RefreshOccurrenceDataByKey();
    }

    /**
     * Create an instance of {@link SaveUserRawResponse }
     * 
     */
    public SaveUserRawResponse createSaveUserRawResponse() {
        return new SaveUserRawResponse();
    }

    /**
     * Create an instance of {@link SaveUserResponse }
     * 
     */
    public SaveUserResponse createSaveUserResponse() {
        return new SaveUserResponse();
    }

    /**
     * Create an instance of {@link GetQueryData }
     * 
     */
    public GetQueryData createGetQueryData() {
        return new GetQueryData();
    }

    /**
     * Create an instance of {@link GetLexiconTextResponse }
     * 
     */
    public GetLexiconTextResponse createGetLexiconTextResponse() {
        return new GetLexiconTextResponse();
    }

    /**
     * Create an instance of {@link UserManagerLogin }
     * 
     */
    public UserManagerLogin createUserManagerLogin() {
        return new UserManagerLogin();
    }

    /**
     * Create an instance of {@link GetQueryResult }
     * 
     */
    public GetQueryResult createGetQueryResult() {
        return new GetQueryResult();
    }

    /**
     * Create an instance of {@link GetUnitsResponse }
     * 
     */
    public GetUnitsResponse createGetUnitsResponse() {
        return new GetUnitsResponse();
    }

    /**
     * Create an instance of {@link GetImpersonatedUserCredentials }
     * 
     */
    public GetImpersonatedUserCredentials createGetImpersonatedUserCredentials() {
        return new GetImpersonatedUserCredentials();
    }

    /**
     * Create an instance of {@link ExecuteQueryObjectRawResponse }
     * 
     */
    public ExecuteQueryObjectRawResponse createExecuteQueryObjectRawResponse() {
        return new ExecuteQueryObjectRawResponse();
    }

    /**
     * Create an instance of {@link GetQueryResponse }
     * 
     */
    public GetQueryResponse createGetQueryResponse() {
        return new GetQueryResponse();
    }

    /**
     * Create an instance of {@link GetAllSubRepositories }
     * 
     */
    public GetAllSubRepositories createGetAllSubRepositories() {
        return new GetAllSubRepositories();
    }

    /**
     * Create an instance of {@link GetExecutedQueryObjectResponse }
     * 
     */
    public GetExecutedQueryObjectResponse createGetExecutedQueryObjectResponse() {
        return new GetExecutedQueryObjectResponse();
    }

    /**
     * Create an instance of {@link GetValues }
     * 
     */
    public GetValues createGetValues() {
        return new GetValues();
    }

    /**
     * Create an instance of {@link GetQueryByExampleObjectResponse }
     * 
     */
    public GetQueryByExampleObjectResponse createGetQueryByExampleObjectResponse() {
        return new GetQueryByExampleObjectResponse();
    }

    /**
     * Create an instance of {@link ExecuteQueryAsTable }
     * 
     */
    public ExecuteQueryAsTable createExecuteQueryAsTable() {
        return new ExecuteQueryAsTable();
    }

    /**
     * Create an instance of {@link GetAttachmentsDetailsByKey }
     * 
     */
    public GetAttachmentsDetailsByKey createGetAttachmentsDetailsByKey() {
        return new GetAttachmentsDetailsByKey();
    }

    /**
     * Create an instance of {@link GetOccurrenceAsPDF }
     * 
     */
    public GetOccurrenceAsPDF createGetOccurrenceAsPDF() {
        return new GetOccurrenceAsPDF();
    }

    /**
     * Create an instance of {@link GetEntity }
     * 
     */
    public GetEntity createGetEntity() {
        return new GetEntity();
    }

    /**
     * Create an instance of {@link GetUser }
     * 
     */
    public GetUser createGetUser() {
        return new GetUser();
    }

    /**
     * Create an instance of {@link IsValidToken }
     * 
     */
    public IsValidToken createIsValidToken() {
        return new IsValidToken();
    }

    /**
     * Create an instance of {@link LoginResponse }
     * 
     */
    public LoginResponse createLoginResponse() {
        return new LoginResponse();
    }

    /**
     * Create an instance of {@link IsValidQueryResult }
     * 
     */
    public IsValidQueryResult createIsValidQueryResult() {
        return new IsValidQueryResult();
    }

    /**
     * Create an instance of {@link GetValuesResponse }
     * 
     */
    public GetValuesResponse createGetValuesResponse() {
        return new GetValuesResponse();
    }

    /**
     * Create an instance of {@link SaveUserInfoResponse }
     * 
     */
    public SaveUserInfoResponse createSaveUserInfoResponse() {
        return new SaveUserInfoResponse();
    }

    /**
     * Create an instance of {@link GetLibraries }
     * 
     */
    public GetLibraries createGetLibraries() {
        return new GetLibraries();
    }

    /**
     * Create an instance of {@link UserManagerLoginResponse }
     * 
     */
    public UserManagerLoginResponse createUserManagerLoginResponse() {
        return new UserManagerLoginResponse();
    }

    /**
     * Create an instance of {@link About }
     * 
     */
    public About createAbout() {
        return new About();
    }

    /**
     * Create an instance of {@link ExecuteQueryObjectAsTable }
     * 
     */
    public ExecuteQueryObjectAsTable createExecuteQueryObjectAsTable() {
        return new ExecuteQueryObjectAsTable();
    }

    /**
     * Create an instance of {@link GetOccurrenceDataResponse }
     * 
     */
    public GetOccurrenceDataResponse createGetOccurrenceDataResponse() {
        return new GetOccurrenceDataResponse();
    }

    /**
     * Create an instance of {@link ExecuteQuery }
     * 
     */
    public ExecuteQuery createExecuteQuery() {
        return new ExecuteQuery();
    }

    /**
     * Create an instance of {@link LoginByInfo }
     * 
     */
    public LoginByInfo createLoginByInfo() {
        return new LoginByInfo();
    }

    /**
     * Create an instance of {@link ExecuteQueryObjectAsTableRaw }
     * 
     */
    public ExecuteQueryObjectAsTableRaw createExecuteQueryObjectAsTableRaw() {
        return new ExecuteQueryObjectAsTableRaw();
    }

    /**
     * Create an instance of {@link GetEntityResponse }
     * 
     */
    public GetEntityResponse createGetEntityResponse() {
        return new GetEntityResponse();
    }

    /**
     * Create an instance of {@link GetQueryByExampleObject }
     * 
     */
    public GetQueryByExampleObject createGetQueryByExampleObject() {
        return new GetQueryByExampleObject();
    }

    /**
     * Create an instance of {@link GetOccurrencePrintingTemplates }
     * 
     */
    public GetOccurrencePrintingTemplates createGetOccurrencePrintingTemplates() {
        return new GetOccurrencePrintingTemplates();
    }

    /**
     * Create an instance of {@link GetUsersResponse }
     * 
     */
    public GetUsersResponse createGetUsersResponse() {
        return new GetUsersResponse();
    }

    /**
     * Create an instance of {@link GetUnits }
     * 
     */
    public GetUnits createGetUnits() {
        return new GetUnits();
    }

    /**
     * Create an instance of {@link DeleteUser }
     * 
     */
    public DeleteUser createDeleteUser() {
        return new DeleteUser();
    }

    /**
     * Create an instance of {@link GetExecutedQueryObject }
     * 
     */
    public GetExecutedQueryObject createGetExecutedQueryObject() {
        return new GetExecutedQueryObject();
    }

    /**
     * Create an instance of {@link SetAttributeUnitID }
     * 
     */
    public SetAttributeUnitID createSetAttributeUnitID() {
        return new SetAttributeUnitID();
    }

    /**
     * Create an instance of {@link GetOccurrencePrintingTemplatesResponse }
     * 
     */
    public GetOccurrencePrintingTemplatesResponse createGetOccurrencePrintingTemplatesResponse() {
        return new GetOccurrencePrintingTemplatesResponse();
    }

    /**
     * Create an instance of {@link GetAttachmentsDetailsByKeyResponse }
     * 
     */
    public GetAttachmentsDetailsByKeyResponse createGetAttachmentsDetailsByKeyResponse() {
        return new GetAttachmentsDetailsByKeyResponse();
    }

    /**
     * Create an instance of {@link SaveUserRaw }
     * 
     */
    public SaveUserRaw createSaveUserRaw() {
        return new SaveUserRaw();
    }

    /**
     * Create an instance of {@link RefreshOccurrenceDataByKeyResponse }
     * 
     */
    public RefreshOccurrenceDataByKeyResponse createRefreshOccurrenceDataByKeyResponse() {
        return new RefreshOccurrenceDataByKeyResponse();
    }

    /**
     * Create an instance of {@link GetExecutedQuery }
     * 
     */
    public GetExecutedQuery createGetExecutedQuery() {
        return new GetExecutedQuery();
    }

    /**
     * Create an instance of {@link GetQuery }
     * 
     */
    public GetQuery createGetQuery() {
        return new GetQuery();
    }

    /**
     * Create an instance of {@link FormatOccurrenceWithTemplateResponse }
     * 
     */
    public FormatOccurrenceWithTemplateResponse createFormatOccurrenceWithTemplateResponse() {
        return new FormatOccurrenceWithTemplateResponse();
    }

    /**
     * Create an instance of {@link UserManagerLoginByInfoResponse }
     * 
     */
    public UserManagerLoginByInfoResponse createUserManagerLoginByInfoResponse() {
        return new UserManagerLoginByInfoResponse();
    }

    /**
     * Create an instance of {@link SaveUserInfo }
     * 
     */
    public SaveUserInfo createSaveUserInfo() {
        return new SaveUserInfo();
    }

    /**
     * Create an instance of {@link GetAttributeResponse }
     * 
     */
    public GetAttributeResponse createGetAttributeResponse() {
        return new GetAttributeResponse();
    }

    /**
     * Create an instance of {@link GetOccurrence }
     * 
     */
    public GetOccurrence createGetOccurrence() {
        return new GetOccurrence();
    }

    /**
     * Create an instance of {@link GetQueryDataResponse }
     * 
     */
    public GetQueryDataResponse createGetQueryDataResponse() {
        return new GetQueryDataResponse();
    }

    /**
     * Create an instance of {@link ExecuteQueryAsTableResponse }
     * 
     */
    public ExecuteQueryAsTableResponse createExecuteQueryAsTableResponse() {
        return new ExecuteQueryAsTableResponse();
    }

    /**
     * Create an instance of {@link GetUserInfoResponse }
     * 
     */
    public GetUserInfoResponse createGetUserInfoResponse() {
        return new GetUserInfoResponse();
    }

    /**
     * Create an instance of {@link GetAttachmentResponse }
     * 
     */
    public GetAttachmentResponse createGetAttachmentResponse() {
        return new GetAttachmentResponse();
    }

    /**
     * Create an instance of {@link ExecuteQueryCountResponse }
     * 
     */
    public ExecuteQueryCountResponse createExecuteQueryCountResponse() {
        return new ExecuteQueryCountResponse();
    }

    /**
     * Create an instance of {@link GetQueryByExampleData }
     * 
     */
    public GetQueryByExampleData createGetQueryByExampleData() {
        return new GetQueryByExampleData();
    }

    /**
     * Create an instance of {@link GetLexiconText }
     * 
     */
    public GetLexiconText createGetLexiconText() {
        return new GetLexiconText();
    }

    /**
     * Create an instance of {@link GetAttributeValue }
     * 
     */
    public GetAttributeValue createGetAttributeValue() {
        return new GetAttributeValue();
    }

    /**
     * Create an instance of {@link ExecuteQueryObjectAsTableRawResponse }
     * 
     */
    public ExecuteQueryObjectAsTableRawResponse createExecuteQueryObjectAsTableRawResponse() {
        return new ExecuteQueryObjectAsTableRawResponse();
    }

    /**
     * Create an instance of {@link GetEntities }
     * 
     */
    public GetEntities createGetEntities() {
        return new GetEntities();
    }

    /**
     * Create an instance of {@link GetViews }
     * 
     */
    public GetViews createGetViews() {
        return new GetViews();
    }

    /**
     * Create an instance of {@link GetQueriesResponse }
     * 
     */
    public GetQueriesResponse createGetQueriesResponse() {
        return new GetQueriesResponse();
    }

    /**
     * Create an instance of {@link GetUserResponse }
     * 
     */
    public GetUserResponse createGetUserResponse() {
        return new GetUserResponse();
    }

    /**
     * Create an instance of {@link SetAttributeUnitIDResponse }
     * 
     */
    public SetAttributeUnitIDResponse createSetAttributeUnitIDResponse() {
        return new SetAttributeUnitIDResponse();
    }

    /**
     * Create an instance of {@link GetQueryResultResponse }
     * 
     */
    public GetQueryResultResponse createGetQueryResultResponse() {
        return new GetQueryResultResponse();
    }

    /**
     * Create an instance of {@link GetQueries }
     * 
     */
    public GetQueries createGetQueries() {
        return new GetQueries();
    }

    /**
     * Create an instance of {@link GetValuePathResponse }
     * 
     */
    public GetValuePathResponse createGetValuePathResponse() {
        return new GetValuePathResponse();
    }

    /**
     * Create an instance of {@link GetExecutedQueryResponse }
     * 
     */
    public GetExecutedQueryResponse createGetExecutedQueryResponse() {
        return new GetExecutedQueryResponse();
    }

    /**
     * Create an instance of {@link GetAllSubRepositoriesResponse }
     * 
     */
    public GetAllSubRepositoriesResponse createGetAllSubRepositoriesResponse() {
        return new GetAllSubRepositoriesResponse();
    }

    /**
     * Create an instance of {@link ReleaseQueryResultResponse }
     * 
     */
    public ReleaseQueryResultResponse createReleaseQueryResultResponse() {
        return new ReleaseQueryResultResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "unsignedLong")
    public JAXBElement<BigInteger> createUnsignedLong(BigInteger value) {
        return new JAXBElement<BigInteger>(_UnsignedLong_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Short }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "unsignedByte")
    public JAXBElement<Short> createUnsignedByte(Short value) {
        return new JAXBElement<Short>(_UnsignedByte_QNAME, Short.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Long }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "unsignedInt")
    public JAXBElement<Long> createUnsignedInt(Long value) {
        return new JAXBElement<Long>(_UnsignedInt_QNAME, Long.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "char")
    public JAXBElement<Integer> createChar(Integer value) {
        return new JAXBElement<Integer>(_Char_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Short }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "short")
    public JAXBElement<Short> createShort(Short value) {
        return new JAXBElement<Short>(_Short_QNAME, Short.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "guid")
    public JAXBElement<String> createGuid(String value) {
        return new JAXBElement<String>(_Guid_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "unsignedShort")
    public JAXBElement<Integer> createUnsignedShort(Integer value) {
        return new JAXBElement<Integer>(_UnsignedShort_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ResultReturnCode }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/ECCAIRS5.Repository.Web.API", name = "ResultReturnCode")
    public JAXBElement<ResultReturnCode> createResultReturnCode(ResultReturnCode value) {
        return new JAXBElement<ResultReturnCode>(_ResultReturnCode_QNAME, ResultReturnCode.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "decimal")
    public JAXBElement<BigDecimal> createDecimal(BigDecimal value) {
        return new JAXBElement<BigDecimal>(_Decimal_QNAME, BigDecimal.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "boolean")
    public JAXBElement<Boolean> createBoolean(Boolean value) {
        return new JAXBElement<Boolean>(_Boolean_QNAME, Boolean.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Duration }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "duration")
    public JAXBElement<Duration> createDuration(Duration value) {
        return new JAXBElement<Duration>(_Duration_QNAME, Duration.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "base64Binary")
    public JAXBElement<byte[]> createBase64Binary(byte[] value) {
        return new JAXBElement<byte[]>(_Base64Binary_QNAME, byte[].class, null, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "int")
    public JAXBElement<Integer> createInt(Integer value) {
        return new JAXBElement<Integer>(_Int_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Long }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "long")
    public JAXBElement<Long> createLong(Long value) {
        return new JAXBElement<Long>(_Long_QNAME, Long.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "anyURI")
    public JAXBElement<String> createAnyURI(String value) {
        return new JAXBElement<String>(_AnyURI_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Float }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "float")
    public JAXBElement<Float> createFloat(Float value) {
        return new JAXBElement<Float>(_Float_QNAME, Float.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EwaResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/ECCAIRS5.Repository.Web.API", name = "ewaResult")
    public JAXBElement<EwaResult> createEwaResult(EwaResult value) {
        return new JAXBElement<EwaResult>(_EwaResult_QNAME, EwaResult.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "dateTime")
    public JAXBElement<XMLGregorianCalendar> createDateTime(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_DateTime_QNAME, XMLGregorianCalendar.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Byte }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "byte")
    public JAXBElement<Byte> createByte(Byte value) {
        return new JAXBElement<Byte>(_Byte_QNAME, Byte.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Double }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "double")
    public JAXBElement<Double> createDouble(Double value) {
        return new JAXBElement<Double>(_Double_QNAME, Double.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QName }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "QName")
    public JAXBElement<QName> createQName(QName value) {
        return new JAXBElement<QName>(_QName_QNAME, QName.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "anyType")
    public JAXBElement<Object> createAnyType(Object value) {
        return new JAXBElement<Object>(_AnyType_QNAME, Object.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "string")
    public JAXBElement<String> createString(String value) {
        return new JAXBElement<String>(_String_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EwaResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "GetImpersonatedUserCredentialsResult", scope = GetImpersonatedUserCredentialsResponse.class)
    public JAXBElement<EwaResult> createGetImpersonatedUserCredentialsResponseGetImpersonatedUserCredentialsResult(EwaResult value) {
        return new JAXBElement<EwaResult>(_GetImpersonatedUserCredentialsResponseGetImpersonatedUserCredentialsResult_QNAME, EwaResult.class, GetImpersonatedUserCredentialsResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EwaResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "CreateUserResult", scope = CreateUserResponse.class)
    public JAXBElement<EwaResult> createCreateUserResponseCreateUserResult(EwaResult value) {
        return new JAXBElement<EwaResult>(_CreateUserResponseCreateUserResult_QNAME, EwaResult.class, CreateUserResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "loginInfo", scope = UserManagerLoginByInfo.class)
    public JAXBElement<String> createUserManagerLoginByInfoLoginInfo(String value) {
        return new JAXBElement<String>(_UserManagerLoginByInfoLoginInfo_QNAME, String.class, UserManagerLoginByInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "userToken", scope = GetQueryObject.class)
    public JAXBElement<String> createGetQueryObjectUserToken(String value) {
        return new JAXBElement<String>(_GetQueryObjectUserToken_QNAME, String.class, GetQueryObject.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "libraryName", scope = GetQueryObject.class)
    public JAXBElement<String> createGetQueryObjectLibraryName(String value) {
        return new JAXBElement<String>(_GetQueryObjectLibraryName_QNAME, String.class, GetQueryObject.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "queryName", scope = GetQueryObject.class)
    public JAXBElement<String> createGetQueryObjectQueryName(String value) {
        return new JAXBElement<String>(_GetQueryObjectQueryName_QNAME, String.class, GetQueryObject.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "userToken", scope = GetOccurrenceByKey.class)
    public JAXBElement<String> createGetOccurrenceByKeyUserToken(String value) {
        return new JAXBElement<String>(_GetQueryObjectUserToken_QNAME, String.class, GetOccurrenceByKey.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "occurrenceKey", scope = GetOccurrenceByKey.class)
    public JAXBElement<String> createGetOccurrenceByKeyOccurrenceKey(String value) {
        return new JAXBElement<String>(_GetOccurrenceByKeyOccurrenceKey_QNAME, String.class, GetOccurrenceByKey.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "userToken", scope = GetQueries.class)
    public JAXBElement<String> createGetQueriesUserToken(String value) {
        return new JAXBElement<String>(_GetQueryObjectUserToken_QNAME, String.class, GetQueries.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "libraryName", scope = GetQueries.class)
    public JAXBElement<String> createGetQueriesLibraryName(String value) {
        return new JAXBElement<String>(_GetQueryObjectLibraryName_QNAME, String.class, GetQueries.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EwaResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "GetAttachmentsDetailsByKeyResult", scope = GetAttachmentsDetailsByKeyResponse.class)
    public JAXBElement<EwaResult> createGetAttachmentsDetailsByKeyResponseGetAttachmentsDetailsByKeyResult(EwaResult value) {
        return new JAXBElement<EwaResult>(_GetAttachmentsDetailsByKeyResponseGetAttachmentsDetailsByKeyResult_QNAME, EwaResult.class, GetAttachmentsDetailsByKeyResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EwaResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "ExecuteQueryObjectResult", scope = ExecuteQueryObjectResponse.class)
    public JAXBElement<EwaResult> createExecuteQueryObjectResponseExecuteQueryObjectResult(EwaResult value) {
        return new JAXBElement<EwaResult>(_ExecuteQueryObjectResponseExecuteQueryObjectResult_QNAME, EwaResult.class, ExecuteQueryObjectResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EwaResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "SaveUserResult", scope = SaveUserResponse.class)
    public JAXBElement<EwaResult> createSaveUserResponseSaveUserResult(EwaResult value) {
        return new JAXBElement<EwaResult>(_SaveUserResponseSaveUserResult_QNAME, EwaResult.class, SaveUserResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "userToken", scope = ExecuteQueryObject.class)
    public JAXBElement<String> createExecuteQueryObjectUserToken(String value) {
        return new JAXBElement<String>(_GetQueryObjectUserToken_QNAME, String.class, ExecuteQueryObject.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "queryObject", scope = ExecuteQueryObject.class)
    public JAXBElement<String> createExecuteQueryObjectQueryObject(String value) {
        return new JAXBElement<String>(_ExecuteQueryObjectQueryObject_QNAME, String.class, ExecuteQueryObject.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EwaResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "GetQueryByExampleObjectResult", scope = GetQueryByExampleObjectResponse.class)
    public JAXBElement<EwaResult> createGetQueryByExampleObjectResponseGetQueryByExampleObjectResult(EwaResult value) {
        return new JAXBElement<EwaResult>(_GetQueryByExampleObjectResponseGetQueryByExampleObjectResult_QNAME, EwaResult.class, GetQueryByExampleObjectResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EwaResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "GetOccurrenceByKeyResult", scope = GetOccurrenceByKeyResponse.class)
    public JAXBElement<EwaResult> createGetOccurrenceByKeyResponseGetOccurrenceByKeyResult(EwaResult value) {
        return new JAXBElement<EwaResult>(_GetOccurrenceByKeyResponseGetOccurrenceByKeyResult_QNAME, EwaResult.class, GetOccurrenceByKeyResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EwaResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "UserManagerLoginResult", scope = UserManagerLoginResponse.class)
    public JAXBElement<EwaResult> createUserManagerLoginResponseUserManagerLoginResult(EwaResult value) {
        return new JAXBElement<EwaResult>(_UserManagerLoginResponseUserManagerLoginResult_QNAME, EwaResult.class, UserManagerLoginResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EwaResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "AttachmentExistsResult", scope = AttachmentExistsResponse.class)
    public JAXBElement<EwaResult> createAttachmentExistsResponseAttachmentExistsResult(EwaResult value) {
        return new JAXBElement<EwaResult>(_AttachmentExistsResponseAttachmentExistsResult_QNAME, EwaResult.class, AttachmentExistsResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "userToken", scope = GetViews.class)
    public JAXBElement<String> createGetViewsUserToken(String value) {
        return new JAXBElement<String>(_GetQueryObjectUserToken_QNAME, String.class, GetViews.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "eccairsNumber", scope = GetViews.class)
    public JAXBElement<String> createGetViewsEccairsNumber(String value) {
        return new JAXBElement<String>(_GetViewsEccairsNumber_QNAME, String.class, GetViews.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "userToken", scope = GetOccurrencePrintingTemplates.class)
    public JAXBElement<String> createGetOccurrencePrintingTemplatesUserToken(String value) {
        return new JAXBElement<String>(_GetQueryObjectUserToken_QNAME, String.class, GetOccurrencePrintingTemplates.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EwaResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "GetQueriesByExampleResult", scope = GetQueriesByExampleResponse.class)
    public JAXBElement<EwaResult> createGetQueriesByExampleResponseGetQueriesByExampleResult(EwaResult value) {
        return new JAXBElement<EwaResult>(_GetQueriesByExampleResponseGetQueriesByExampleResult_QNAME, EwaResult.class, GetQueriesByExampleResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "userToken", scope = GetExecutedQuery.class)
    public JAXBElement<String> createGetExecutedQueryUserToken(String value) {
        return new JAXBElement<String>(_GetQueryObjectUserToken_QNAME, String.class, GetExecutedQuery.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "operationID", scope = GetExecutedQuery.class)
    public JAXBElement<String> createGetExecutedQueryOperationID(String value) {
        return new JAXBElement<String>(_GetExecutedQueryOperationID_QNAME, String.class, GetExecutedQuery.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EwaResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "GetAttributesResult", scope = GetAttributesResponse.class)
    public JAXBElement<EwaResult> createGetAttributesResponseGetAttributesResult(EwaResult value) {
        return new JAXBElement<EwaResult>(_GetAttributesResponseGetAttributesResult_QNAME, EwaResult.class, GetAttributesResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EwaResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "GetQueriesResult", scope = GetQueriesResponse.class)
    public JAXBElement<EwaResult> createGetQueriesResponseGetQueriesResult(EwaResult value) {
        return new JAXBElement<EwaResult>(_GetQueriesResponseGetQueriesResult_QNAME, EwaResult.class, GetQueriesResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EwaResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "RefreshOccurrenceDataByKeyResult", scope = RefreshOccurrenceDataByKeyResponse.class)
    public JAXBElement<EwaResult> createRefreshOccurrenceDataByKeyResponseRefreshOccurrenceDataByKeyResult(EwaResult value) {
        return new JAXBElement<EwaResult>(_RefreshOccurrenceDataByKeyResponseRefreshOccurrenceDataByKeyResult_QNAME, EwaResult.class, RefreshOccurrenceDataByKeyResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "userToken", scope = GetOccurrenceData.class)
    public JAXBElement<String> createGetOccurrenceDataUserToken(String value) {
        return new JAXBElement<String>(_GetQueryObjectUserToken_QNAME, String.class, GetOccurrenceData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "eccairsNumber", scope = GetOccurrenceData.class)
    public JAXBElement<String> createGetOccurrenceDataEccairsNumber(String value) {
        return new JAXBElement<String>(_GetViewsEccairsNumber_QNAME, String.class, GetOccurrenceData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EwaResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "RefreshOccurrenceResult", scope = RefreshOccurrenceResponse.class)
    public JAXBElement<EwaResult> createRefreshOccurrenceResponseRefreshOccurrenceResult(EwaResult value) {
        return new JAXBElement<EwaResult>(_RefreshOccurrenceResponseRefreshOccurrenceResult_QNAME, EwaResult.class, RefreshOccurrenceResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EwaResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "GetViewsResult", scope = GetViewsResponse.class)
    public JAXBElement<EwaResult> createGetViewsResponseGetViewsResult(EwaResult value) {
        return new JAXBElement<EwaResult>(_GetViewsResponseGetViewsResult_QNAME, EwaResult.class, GetViewsResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "userManagerToken", scope = GetUsers.class)
    public JAXBElement<String> createGetUsersUserManagerToken(String value) {
        return new JAXBElement<String>(_GetUsersUserManagerToken_QNAME, String.class, GetUsers.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EwaResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "LogoutResult", scope = LogoutResponse.class)
    public JAXBElement<EwaResult> createLogoutResponseLogoutResult(EwaResult value) {
        return new JAXBElement<EwaResult>(_LogoutResponseLogoutResult_QNAME, EwaResult.class, LogoutResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EwaResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "GetLexiconTextResult", scope = GetLexiconTextResponse.class)
    public JAXBElement<EwaResult> createGetLexiconTextResponseGetLexiconTextResult(EwaResult value) {
        return new JAXBElement<EwaResult>(_GetLexiconTextResponseGetLexiconTextResult_QNAME, EwaResult.class, GetLexiconTextResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "password", scope = Login.class)
    public JAXBElement<String> createLoginPassword(String value) {
        return new JAXBElement<String>(_LoginPassword_QNAME, String.class, Login.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "language", scope = Login.class)
    public JAXBElement<String> createLoginLanguage(String value) {
        return new JAXBElement<String>(_LoginLanguage_QNAME, String.class, Login.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "username", scope = Login.class)
    public JAXBElement<String> createLoginUsername(String value) {
        return new JAXBElement<String>(_LoginUsername_QNAME, String.class, Login.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "repository", scope = Login.class)
    public JAXBElement<String> createLoginRepository(String value) {
        return new JAXBElement<String>(_LoginRepository_QNAME, String.class, Login.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EwaResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "GetAllSubRepositoriesResult", scope = GetAllSubRepositoriesResponse.class)
    public JAXBElement<EwaResult> createGetAllSubRepositoriesResponseGetAllSubRepositoriesResult(EwaResult value) {
        return new JAXBElement<EwaResult>(_GetAllSubRepositoriesResponseGetAllSubRepositoriesResult_QNAME, EwaResult.class, GetAllSubRepositoriesResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "userToken", scope = IsValidQueryResult.class)
    public JAXBElement<String> createIsValidQueryResultUserToken(String value) {
        return new JAXBElement<String>(_GetQueryObjectUserToken_QNAME, String.class, IsValidQueryResult.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "operationID", scope = IsValidQueryResult.class)
    public JAXBElement<String> createIsValidQueryResultOperationID(String value) {
        return new JAXBElement<String>(_GetExecutedQueryOperationID_QNAME, String.class, IsValidQueryResult.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EwaResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "GetQueryByExampleDataResult", scope = GetQueryByExampleDataResponse.class)
    public JAXBElement<EwaResult> createGetQueryByExampleDataResponseGetQueryByExampleDataResult(EwaResult value) {
        return new JAXBElement<EwaResult>(_GetQueryByExampleDataResponseGetQueryByExampleDataResult_QNAME, EwaResult.class, GetQueryByExampleDataResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "userToken", scope = FormatOccurrenceWithTemplate.class)
    public JAXBElement<String> createFormatOccurrenceWithTemplateUserToken(String value) {
        return new JAXBElement<String>(_GetQueryObjectUserToken_QNAME, String.class, FormatOccurrenceWithTemplate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "templateName", scope = FormatOccurrenceWithTemplate.class)
    public JAXBElement<String> createFormatOccurrenceWithTemplateTemplateName(String value) {
        return new JAXBElement<String>(_FormatOccurrenceWithTemplateTemplateName_QNAME, String.class, FormatOccurrenceWithTemplate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "eccairsNumber", scope = FormatOccurrenceWithTemplate.class)
    public JAXBElement<String> createFormatOccurrenceWithTemplateEccairsNumber(String value) {
        return new JAXBElement<String>(_GetViewsEccairsNumber_QNAME, String.class, FormatOccurrenceWithTemplate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "attachmentKey", scope = AttachmentExists.class)
    public JAXBElement<String> createAttachmentExistsAttachmentKey(String value) {
        return new JAXBElement<String>(_AttachmentExistsAttachmentKey_QNAME, String.class, AttachmentExists.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "userToken", scope = AttachmentExists.class)
    public JAXBElement<String> createAttachmentExistsUserToken(String value) {
        return new JAXBElement<String>(_GetQueryObjectUserToken_QNAME, String.class, AttachmentExists.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EwaResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "AboutResult", scope = AboutResponse.class)
    public JAXBElement<EwaResult> createAboutResponseAboutResult(EwaResult value) {
        return new JAXBElement<EwaResult>(_AboutResponseAboutResult_QNAME, EwaResult.class, AboutResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EwaResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "GetOccurrencePrintingTemplatesResult", scope = GetOccurrencePrintingTemplatesResponse.class)
    public JAXBElement<EwaResult> createGetOccurrencePrintingTemplatesResponseGetOccurrencePrintingTemplatesResult(EwaResult value) {
        return new JAXBElement<EwaResult>(_GetOccurrencePrintingTemplatesResponseGetOccurrencePrintingTemplatesResult_QNAME, EwaResult.class, GetOccurrencePrintingTemplatesResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EwaResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "ExecuteQueryAsTableResult", scope = ExecuteQueryAsTableResponse.class)
    public JAXBElement<EwaResult> createExecuteQueryAsTableResponseExecuteQueryAsTableResult(EwaResult value) {
        return new JAXBElement<EwaResult>(_ExecuteQueryAsTableResponseExecuteQueryAsTableResult_QNAME, EwaResult.class, ExecuteQueryAsTableResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "userToken", scope = GetUnits.class)
    public JAXBElement<String> createGetUnitsUserToken(String value) {
        return new JAXBElement<String>(_GetQueryObjectUserToken_QNAME, String.class, GetUnits.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "userManagerToken", scope = CreateUser.class)
    public JAXBElement<String> createCreateUserUserManagerToken(String value) {
        return new JAXBElement<String>(_GetUsersUserManagerToken_QNAME, String.class, CreateUser.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EwaResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "ExecuteQueryCountResult", scope = ExecuteQueryCountResponse.class)
    public JAXBElement<EwaResult> createExecuteQueryCountResponseExecuteQueryCountResult(EwaResult value) {
        return new JAXBElement<EwaResult>(_ExecuteQueryCountResponseExecuteQueryCountResult_QNAME, EwaResult.class, ExecuteQueryCountResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EwaResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "LoginByInfoResult", scope = LoginByInfoResponse.class)
    public JAXBElement<EwaResult> createLoginByInfoResponseLoginByInfoResult(EwaResult value) {
        return new JAXBElement<EwaResult>(_LoginByInfoResponseLoginByInfoResult_QNAME, EwaResult.class, LoginByInfoResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EwaResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "ExecuteQueryObjectCountRawResult", scope = ExecuteQueryObjectCountRawResponse.class)
    public JAXBElement<EwaResult> createExecuteQueryObjectCountRawResponseExecuteQueryObjectCountRawResult(EwaResult value) {
        return new JAXBElement<EwaResult>(_ExecuteQueryObjectCountRawResponseExecuteQueryObjectCountRawResult_QNAME, EwaResult.class, ExecuteQueryObjectCountRawResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "userToken", scope = GetQueryResult.class)
    public JAXBElement<String> createGetQueryResultUserToken(String value) {
        return new JAXBElement<String>(_GetQueryObjectUserToken_QNAME, String.class, GetQueryResult.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "operationID", scope = GetQueryResult.class)
    public JAXBElement<String> createGetQueryResultOperationID(String value) {
        return new JAXBElement<String>(_GetExecutedQueryOperationID_QNAME, String.class, GetQueryResult.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "order", scope = GetQueryResult.class)
    public JAXBElement<String> createGetQueryResultOrder(String value) {
        return new JAXBElement<String>(_GetQueryResultOrder_QNAME, String.class, GetQueryResult.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EwaResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "DeleteUserResult", scope = DeleteUserResponse.class)
    public JAXBElement<EwaResult> createDeleteUserResponseDeleteUserResult(EwaResult value) {
        return new JAXBElement<EwaResult>(_DeleteUserResponseDeleteUserResult_QNAME, EwaResult.class, DeleteUserResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EwaResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "GetExecutedQueryObjectResult", scope = GetExecutedQueryObjectResponse.class)
    public JAXBElement<EwaResult> createGetExecutedQueryObjectResponseGetExecutedQueryObjectResult(EwaResult value) {
        return new JAXBElement<EwaResult>(_GetExecutedQueryObjectResponseGetExecutedQueryObjectResult_QNAME, EwaResult.class, GetExecutedQueryObjectResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "userToken", scope = ExecuteQuery.class)
    public JAXBElement<String> createExecuteQueryUserToken(String value) {
        return new JAXBElement<String>(_GetQueryObjectUserToken_QNAME, String.class, ExecuteQuery.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "libraryName", scope = ExecuteQuery.class)
    public JAXBElement<String> createExecuteQueryLibraryName(String value) {
        return new JAXBElement<String>(_GetQueryObjectLibraryName_QNAME, String.class, ExecuteQuery.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "queryName", scope = ExecuteQuery.class)
    public JAXBElement<String> createExecuteQueryQueryName(String value) {
        return new JAXBElement<String>(_GetQueryObjectQueryName_QNAME, String.class, ExecuteQuery.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "userToken", scope = GetViewImage.class)
    public JAXBElement<String> createGetViewImageUserToken(String value) {
        return new JAXBElement<String>(_GetQueryObjectUserToken_QNAME, String.class, GetViewImage.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "viewName", scope = GetViewImage.class)
    public JAXBElement<String> createGetViewImageViewName(String value) {
        return new JAXBElement<String>(_GetViewImageViewName_QNAME, String.class, GetViewImage.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "userToken", scope = GetAttachmentsDetails.class)
    public JAXBElement<String> createGetAttachmentsDetailsUserToken(String value) {
        return new JAXBElement<String>(_GetQueryObjectUserToken_QNAME, String.class, GetAttachmentsDetails.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "eccairsNumber", scope = GetAttachmentsDetails.class)
    public JAXBElement<String> createGetAttachmentsDetailsEccairsNumber(String value) {
        return new JAXBElement<String>(_GetViewsEccairsNumber_QNAME, String.class, GetAttachmentsDetails.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "attachmentKey", scope = GetAttachment.class)
    public JAXBElement<String> createGetAttachmentAttachmentKey(String value) {
        return new JAXBElement<String>(_AttachmentExistsAttachmentKey_QNAME, String.class, GetAttachment.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "userToken", scope = GetAttachment.class)
    public JAXBElement<String> createGetAttachmentUserToken(String value) {
        return new JAXBElement<String>(_GetQueryObjectUserToken_QNAME, String.class, GetAttachment.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "userToken", scope = GetOccurrence.class)
    public JAXBElement<String> createGetOccurrenceUserToken(String value) {
        return new JAXBElement<String>(_GetQueryObjectUserToken_QNAME, String.class, GetOccurrence.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "eccairsNumber", scope = GetOccurrence.class)
    public JAXBElement<String> createGetOccurrenceEccairsNumber(String value) {
        return new JAXBElement<String>(_GetViewsEccairsNumber_QNAME, String.class, GetOccurrence.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EwaResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "GetEntitiesResult", scope = GetEntitiesResponse.class)
    public JAXBElement<EwaResult> createGetEntitiesResponseGetEntitiesResult(EwaResult value) {
        return new JAXBElement<EwaResult>(_GetEntitiesResponseGetEntitiesResult_QNAME, EwaResult.class, GetEntitiesResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "userManagerToken", scope = DeleteUser.class)
    public JAXBElement<String> createDeleteUserUserManagerToken(String value) {
        return new JAXBElement<String>(_GetUsersUserManagerToken_QNAME, String.class, DeleteUser.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "username", scope = DeleteUser.class)
    public JAXBElement<String> createDeleteUserUsername(String value) {
        return new JAXBElement<String>(_LoginUsername_QNAME, String.class, DeleteUser.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "userToken", scope = GetAttributeValue.class)
    public JAXBElement<String> createGetAttributeValueUserToken(String value) {
        return new JAXBElement<String>(_GetQueryObjectUserToken_QNAME, String.class, GetAttributeValue.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "valueData", scope = GetAttributeValue.class)
    public JAXBElement<String> createGetAttributeValueValueData(String value) {
        return new JAXBElement<String>(_GetAttributeValueValueData_QNAME, String.class, GetAttributeValue.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EwaResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "GetOccurrenceDataByKeyResult", scope = GetOccurrenceDataByKeyResponse.class)
    public JAXBElement<EwaResult> createGetOccurrenceDataByKeyResponseGetOccurrenceDataByKeyResult(EwaResult value) {
        return new JAXBElement<EwaResult>(_GetOccurrenceDataByKeyResponseGetOccurrenceDataByKeyResult_QNAME, EwaResult.class, GetOccurrenceDataByKeyResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EwaResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "ReleaseQueryResultResult", scope = ReleaseQueryResultResponse.class)
    public JAXBElement<EwaResult> createReleaseQueryResultResponseReleaseQueryResultResult(EwaResult value) {
        return new JAXBElement<EwaResult>(_ReleaseQueryResultResponseReleaseQueryResultResult_QNAME, EwaResult.class, ReleaseQueryResultResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "userToken", scope = GetExecutedQueryObject.class)
    public JAXBElement<String> createGetExecutedQueryObjectUserToken(String value) {
        return new JAXBElement<String>(_GetQueryObjectUserToken_QNAME, String.class, GetExecutedQueryObject.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "operationID", scope = GetExecutedQueryObject.class)
    public JAXBElement<String> createGetExecutedQueryObjectOperationID(String value) {
        return new JAXBElement<String>(_GetExecutedQueryOperationID_QNAME, String.class, GetExecutedQueryObject.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EwaResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "GetQueryObjectResult", scope = GetQueryObjectResponse.class)
    public JAXBElement<EwaResult> createGetQueryObjectResponseGetQueryObjectResult(EwaResult value) {
        return new JAXBElement<EwaResult>(_GetQueryObjectResponseGetQueryObjectResult_QNAME, EwaResult.class, GetQueryObjectResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EwaResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "SaveUserRawResult", scope = SaveUserRawResponse.class)
    public JAXBElement<EwaResult> createSaveUserRawResponseSaveUserRawResult(EwaResult value) {
        return new JAXBElement<EwaResult>(_SaveUserRawResponseSaveUserRawResult_QNAME, EwaResult.class, SaveUserRawResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EwaResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "GetRepositoriesResult", scope = GetRepositoriesResponse.class)
    public JAXBElement<EwaResult> createGetRepositoriesResponseGetRepositoriesResult(EwaResult value) {
        return new JAXBElement<EwaResult>(_GetRepositoriesResponseGetRepositoriesResult_QNAME, EwaResult.class, GetRepositoriesResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EwaResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "SaveUserInfoResult", scope = SaveUserInfoResponse.class)
    public JAXBElement<EwaResult> createSaveUserInfoResponseSaveUserInfoResult(EwaResult value) {
        return new JAXBElement<EwaResult>(_SaveUserInfoResponseSaveUserInfoResult_QNAME, EwaResult.class, SaveUserInfoResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EwaResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "GetOccurrenceResult", scope = GetOccurrenceResponse.class)
    public JAXBElement<EwaResult> createGetOccurrenceResponseGetOccurrenceResult(EwaResult value) {
        return new JAXBElement<EwaResult>(_GetOccurrenceResponseGetOccurrenceResult_QNAME, EwaResult.class, GetOccurrenceResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "userToken", scope = RefreshOccurrence.class)
    public JAXBElement<String> createRefreshOccurrenceUserToken(String value) {
        return new JAXBElement<String>(_GetQueryObjectUserToken_QNAME, String.class, RefreshOccurrence.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "eccairsNumber", scope = RefreshOccurrence.class)
    public JAXBElement<String> createRefreshOccurrenceEccairsNumber(String value) {
        return new JAXBElement<String>(_GetViewsEccairsNumber_QNAME, String.class, RefreshOccurrence.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "userToken", scope = Logout.class)
    public JAXBElement<String> createLogoutUserToken(String value) {
        return new JAXBElement<String>(_GetQueryObjectUserToken_QNAME, String.class, Logout.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "userToken", scope = GetQueriesByExample.class)
    public JAXBElement<String> createGetQueriesByExampleUserToken(String value) {
        return new JAXBElement<String>(_GetQueryObjectUserToken_QNAME, String.class, GetQueriesByExample.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "userToken", scope = GetValuePath.class)
    public JAXBElement<String> createGetValuePathUserToken(String value) {
        return new JAXBElement<String>(_GetQueryObjectUserToken_QNAME, String.class, GetValuePath.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EwaResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "GetLibrariesResult", scope = GetLibrariesResponse.class)
    public JAXBElement<EwaResult> createGetLibrariesResponseGetLibrariesResult(EwaResult value) {
        return new JAXBElement<EwaResult>(_GetLibrariesResponseGetLibrariesResult_QNAME, EwaResult.class, GetLibrariesResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "userToken", scope = ReleaseQueryResult.class)
    public JAXBElement<String> createReleaseQueryResultUserToken(String value) {
        return new JAXBElement<String>(_GetQueryObjectUserToken_QNAME, String.class, ReleaseQueryResult.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "operationID", scope = ReleaseQueryResult.class)
    public JAXBElement<String> createReleaseQueryResultOperationID(String value) {
        return new JAXBElement<String>(_GetExecutedQueryOperationID_QNAME, String.class, ReleaseQueryResult.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EwaResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "GetAttributeResult", scope = GetAttributeResponse.class)
    public JAXBElement<EwaResult> createGetAttributeResponseGetAttributeResult(EwaResult value) {
        return new JAXBElement<EwaResult>(_GetAttributeResponseGetAttributeResult_QNAME, EwaResult.class, GetAttributeResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "userToken", scope = GetOccurrenceDataByKey.class)
    public JAXBElement<String> createGetOccurrenceDataByKeyUserToken(String value) {
        return new JAXBElement<String>(_GetQueryObjectUserToken_QNAME, String.class, GetOccurrenceDataByKey.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "occurrenceKey", scope = GetOccurrenceDataByKey.class)
    public JAXBElement<String> createGetOccurrenceDataByKeyOccurrenceKey(String value) {
        return new JAXBElement<String>(_GetOccurrenceByKeyOccurrenceKey_QNAME, String.class, GetOccurrenceDataByKey.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "userToken", scope = GetQueryData.class)
    public JAXBElement<String> createGetQueryDataUserToken(String value) {
        return new JAXBElement<String>(_GetQueryObjectUserToken_QNAME, String.class, GetQueryData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "libraryName", scope = GetQueryData.class)
    public JAXBElement<String> createGetQueryDataLibraryName(String value) {
        return new JAXBElement<String>(_GetQueryObjectLibraryName_QNAME, String.class, GetQueryData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "queryName", scope = GetQueryData.class)
    public JAXBElement<String> createGetQueryDataQueryName(String value) {
        return new JAXBElement<String>(_GetQueryObjectQueryName_QNAME, String.class, GetQueryData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EwaResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "GetUserInfoResult", scope = GetUserInfoResponse.class)
    public JAXBElement<EwaResult> createGetUserInfoResponseGetUserInfoResult(EwaResult value) {
        return new JAXBElement<EwaResult>(_GetUserInfoResponseGetUserInfoResult_QNAME, EwaResult.class, GetUserInfoResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "userToken", scope = FormatOccurrenceWithTemplateByKey.class)
    public JAXBElement<String> createFormatOccurrenceWithTemplateByKeyUserToken(String value) {
        return new JAXBElement<String>(_GetQueryObjectUserToken_QNAME, String.class, FormatOccurrenceWithTemplateByKey.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "templateName", scope = FormatOccurrenceWithTemplateByKey.class)
    public JAXBElement<String> createFormatOccurrenceWithTemplateByKeyTemplateName(String value) {
        return new JAXBElement<String>(_FormatOccurrenceWithTemplateTemplateName_QNAME, String.class, FormatOccurrenceWithTemplateByKey.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "occurrenceKey", scope = FormatOccurrenceWithTemplateByKey.class)
    public JAXBElement<String> createFormatOccurrenceWithTemplateByKeyOccurrenceKey(String value) {
        return new JAXBElement<String>(_GetOccurrenceByKeyOccurrenceKey_QNAME, String.class, FormatOccurrenceWithTemplateByKey.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "userToken", scope = GetAttributes.class)
    public JAXBElement<String> createGetAttributesUserToken(String value) {
        return new JAXBElement<String>(_GetQueryObjectUserToken_QNAME, String.class, GetAttributes.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "userToken", scope = GetOccurrenceAsPDF.class)
    public JAXBElement<String> createGetOccurrenceAsPDFUserToken(String value) {
        return new JAXBElement<String>(_GetQueryObjectUserToken_QNAME, String.class, GetOccurrenceAsPDF.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "viewName", scope = GetOccurrenceAsPDF.class)
    public JAXBElement<String> createGetOccurrenceAsPDFViewName(String value) {
        return new JAXBElement<String>(_GetViewImageViewName_QNAME, String.class, GetOccurrenceAsPDF.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "eccairsNumber", scope = GetOccurrenceAsPDF.class)
    public JAXBElement<String> createGetOccurrenceAsPDFEccairsNumber(String value) {
        return new JAXBElement<String>(_GetViewsEccairsNumber_QNAME, String.class, GetOccurrenceAsPDF.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "userToken", scope = GetQuery.class)
    public JAXBElement<String> createGetQueryUserToken(String value) {
        return new JAXBElement<String>(_GetQueryObjectUserToken_QNAME, String.class, GetQuery.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "operationID", scope = GetQuery.class)
    public JAXBElement<String> createGetQueryOperationID(String value) {
        return new JAXBElement<String>(_GetExecutedQueryOperationID_QNAME, String.class, GetQuery.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "userToken", scope = GetEntity.class)
    public JAXBElement<String> createGetEntityUserToken(String value) {
        return new JAXBElement<String>(_GetQueryObjectUserToken_QNAME, String.class, GetEntity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "userToken", scope = GetUserInfo.class)
    public JAXBElement<String> createGetUserInfoUserToken(String value) {
        return new JAXBElement<String>(_GetQueryObjectUserToken_QNAME, String.class, GetUserInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "rootRepository", scope = GetAllSubRepositories.class)
    public JAXBElement<String> createGetAllSubRepositoriesRootRepository(String value) {
        return new JAXBElement<String>(_GetAllSubRepositoriesRootRepository_QNAME, String.class, GetAllSubRepositories.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "userToken", scope = IsValidToken.class)
    public JAXBElement<String> createIsValidTokenUserToken(String value) {
        return new JAXBElement<String>(_GetQueryObjectUserToken_QNAME, String.class, IsValidToken.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "userToken", scope = ExecuteQueryAsTable.class)
    public JAXBElement<String> createExecuteQueryAsTableUserToken(String value) {
        return new JAXBElement<String>(_GetQueryObjectUserToken_QNAME, String.class, ExecuteQueryAsTable.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "libraryName", scope = ExecuteQueryAsTable.class)
    public JAXBElement<String> createExecuteQueryAsTableLibraryName(String value) {
        return new JAXBElement<String>(_GetQueryObjectLibraryName_QNAME, String.class, ExecuteQueryAsTable.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "queryName", scope = ExecuteQueryAsTable.class)
    public JAXBElement<String> createExecuteQueryAsTableQueryName(String value) {
        return new JAXBElement<String>(_GetQueryObjectQueryName_QNAME, String.class, ExecuteQueryAsTable.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EwaResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "GetValuePathResult", scope = GetValuePathResponse.class)
    public JAXBElement<EwaResult> createGetValuePathResponseGetValuePathResult(EwaResult value) {
        return new JAXBElement<EwaResult>(_GetValuePathResponseGetValuePathResult_QNAME, EwaResult.class, GetValuePathResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EwaResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "GetOccurrenceDataResult", scope = GetOccurrenceDataResponse.class)
    public JAXBElement<EwaResult> createGetOccurrenceDataResponseGetOccurrenceDataResult(EwaResult value) {
        return new JAXBElement<EwaResult>(_GetOccurrenceDataResponseGetOccurrenceDataResult_QNAME, EwaResult.class, GetOccurrenceDataResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/ECCAIRS5.Repository.Web.API", name = "ErrorDetails", scope = EwaResult.class)
    public JAXBElement<String> createEwaResultErrorDetails(String value) {
        return new JAXBElement<String>(_EwaResultErrorDetails_QNAME, String.class, EwaResult.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/ECCAIRS5.Repository.Web.API", name = "Data", scope = EwaResult.class)
    public JAXBElement<String> createEwaResultData(String value) {
        return new JAXBElement<String>(_EwaResultData_QNAME, String.class, EwaResult.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/ECCAIRS5.Repository.Web.API", name = "UserToken", scope = EwaResult.class)
    public JAXBElement<String> createEwaResultUserToken(String value) {
        return new JAXBElement<String>(_EwaResultUserToken_QNAME, String.class, EwaResult.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EwaResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "ExecuteQueryResult", scope = ExecuteQueryResponse.class)
    public JAXBElement<EwaResult> createExecuteQueryResponseExecuteQueryResult(EwaResult value) {
        return new JAXBElement<EwaResult>(_ExecuteQueryResponseExecuteQueryResult_QNAME, EwaResult.class, ExecuteQueryResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EwaResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "UserManagerLoginByInfoResult", scope = UserManagerLoginByInfoResponse.class)
    public JAXBElement<EwaResult> createUserManagerLoginByInfoResponseUserManagerLoginByInfoResult(EwaResult value) {
        return new JAXBElement<EwaResult>(_UserManagerLoginByInfoResponseUserManagerLoginByInfoResult_QNAME, EwaResult.class, UserManagerLoginByInfoResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EwaResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "ExecuteQueryObjectAsTableRawResult", scope = ExecuteQueryObjectAsTableRawResponse.class)
    public JAXBElement<EwaResult> createExecuteQueryObjectAsTableRawResponseExecuteQueryObjectAsTableRawResult(EwaResult value) {
        return new JAXBElement<EwaResult>(_ExecuteQueryObjectAsTableRawResponseExecuteQueryObjectAsTableRawResult_QNAME, EwaResult.class, ExecuteQueryObjectAsTableRawResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "userToken", scope = GetLexiconText.class)
    public JAXBElement<String> createGetLexiconTextUserToken(String value) {
        return new JAXBElement<String>(_GetQueryObjectUserToken_QNAME, String.class, GetLexiconText.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EwaResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "GetQueryDataResult", scope = GetQueryDataResponse.class)
    public JAXBElement<EwaResult> createGetQueryDataResponseGetQueryDataResult(EwaResult value) {
        return new JAXBElement<EwaResult>(_GetQueryDataResponseGetQueryDataResult_QNAME, EwaResult.class, GetQueryDataResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EwaResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "ExecuteQueryObjectRawResult", scope = ExecuteQueryObjectRawResponse.class)
    public JAXBElement<EwaResult> createExecuteQueryObjectRawResponseExecuteQueryObjectRawResult(EwaResult value) {
        return new JAXBElement<EwaResult>(_ExecuteQueryObjectRawResponseExecuteQueryObjectRawResult_QNAME, EwaResult.class, ExecuteQueryObjectRawResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EwaResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "GetUnitsResult", scope = GetUnitsResponse.class)
    public JAXBElement<EwaResult> createGetUnitsResponseGetUnitsResult(EwaResult value) {
        return new JAXBElement<EwaResult>(_GetUnitsResponseGetUnitsResult_QNAME, EwaResult.class, GetUnitsResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EwaResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "GetEntityResult", scope = GetEntityResponse.class)
    public JAXBElement<EwaResult> createGetEntityResponseGetEntityResult(EwaResult value) {
        return new JAXBElement<EwaResult>(_GetEntityResponseGetEntityResult_QNAME, EwaResult.class, GetEntityResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EwaResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "GetViewsByKeyResult", scope = GetViewsByKeyResponse.class)
    public JAXBElement<EwaResult> createGetViewsByKeyResponseGetViewsByKeyResult(EwaResult value) {
        return new JAXBElement<EwaResult>(_GetViewsByKeyResponseGetViewsByKeyResult_QNAME, EwaResult.class, GetViewsByKeyResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "userToken", scope = SetAttributeUnitID.class)
    public JAXBElement<String> createSetAttributeUnitIDUserToken(String value) {
        return new JAXBElement<String>(_GetQueryObjectUserToken_QNAME, String.class, SetAttributeUnitID.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EwaResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "GetQueryResultResult", scope = GetQueryResultResponse.class)
    public JAXBElement<EwaResult> createGetQueryResultResponseGetQueryResultResult(EwaResult value) {
        return new JAXBElement<EwaResult>(_GetQueryResultResponseGetQueryResultResult_QNAME, EwaResult.class, GetQueryResultResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "userToken", scope = GetLibraries.class)
    public JAXBElement<String> createGetLibrariesUserToken(String value) {
        return new JAXBElement<String>(_GetQueryObjectUserToken_QNAME, String.class, GetLibraries.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "userToken", scope = GetQueryByExampleData.class)
    public JAXBElement<String> createGetQueryByExampleDataUserToken(String value) {
        return new JAXBElement<String>(_GetQueryObjectUserToken_QNAME, String.class, GetQueryByExampleData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "queryName", scope = GetQueryByExampleData.class)
    public JAXBElement<String> createGetQueryByExampleDataQueryName(String value) {
        return new JAXBElement<String>(_GetQueryObjectQueryName_QNAME, String.class, GetQueryByExampleData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EwaResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "GetQueryResult", scope = GetQueryResponse.class)
    public JAXBElement<EwaResult> createGetQueryResponseGetQueryResult(EwaResult value) {
        return new JAXBElement<EwaResult>(_GetQueryResponseGetQueryResult_QNAME, EwaResult.class, GetQueryResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "userToken", scope = GetOccurrenceAsPDFByKey.class)
    public JAXBElement<String> createGetOccurrenceAsPDFByKeyUserToken(String value) {
        return new JAXBElement<String>(_GetQueryObjectUserToken_QNAME, String.class, GetOccurrenceAsPDFByKey.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "viewName", scope = GetOccurrenceAsPDFByKey.class)
    public JAXBElement<String> createGetOccurrenceAsPDFByKeyViewName(String value) {
        return new JAXBElement<String>(_GetViewImageViewName_QNAME, String.class, GetOccurrenceAsPDFByKey.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "occurrenceKey", scope = GetOccurrenceAsPDFByKey.class)
    public JAXBElement<String> createGetOccurrenceAsPDFByKeyOccurrenceKey(String value) {
        return new JAXBElement<String>(_GetOccurrenceByKeyOccurrenceKey_QNAME, String.class, GetOccurrenceAsPDFByKey.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "userToken", scope = GetQueryByExampleObject.class)
    public JAXBElement<String> createGetQueryByExampleObjectUserToken(String value) {
        return new JAXBElement<String>(_GetQueryObjectUserToken_QNAME, String.class, GetQueryByExampleObject.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "queryName", scope = GetQueryByExampleObject.class)
    public JAXBElement<String> createGetQueryByExampleObjectQueryName(String value) {
        return new JAXBElement<String>(_GetQueryObjectQueryName_QNAME, String.class, GetQueryByExampleObject.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EwaResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "SetAttributeUnitIDResult", scope = SetAttributeUnitIDResponse.class)
    public JAXBElement<EwaResult> createSetAttributeUnitIDResponseSetAttributeUnitIDResult(EwaResult value) {
        return new JAXBElement<EwaResult>(_SetAttributeUnitIDResponseSetAttributeUnitIDResult_QNAME, EwaResult.class, SetAttributeUnitIDResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "userToken", scope = GetViewsByKey.class)
    public JAXBElement<String> createGetViewsByKeyUserToken(String value) {
        return new JAXBElement<String>(_GetQueryObjectUserToken_QNAME, String.class, GetViewsByKey.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "occurrenceKey", scope = GetViewsByKey.class)
    public JAXBElement<String> createGetViewsByKeyOccurrenceKey(String value) {
        return new JAXBElement<String>(_GetOccurrenceByKeyOccurrenceKey_QNAME, String.class, GetViewsByKey.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EwaResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "ExecuteQueryObjectAsTableResult", scope = ExecuteQueryObjectAsTableResponse.class)
    public JAXBElement<EwaResult> createExecuteQueryObjectAsTableResponseExecuteQueryObjectAsTableResult(EwaResult value) {
        return new JAXBElement<EwaResult>(_ExecuteQueryObjectAsTableResponseExecuteQueryObjectAsTableResult_QNAME, EwaResult.class, ExecuteQueryObjectAsTableResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "userToken", scope = RefreshOccurrenceDataByKey.class)
    public JAXBElement<String> createRefreshOccurrenceDataByKeyUserToken(String value) {
        return new JAXBElement<String>(_GetQueryObjectUserToken_QNAME, String.class, RefreshOccurrenceDataByKey.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "occurrenceKey", scope = RefreshOccurrenceDataByKey.class)
    public JAXBElement<String> createRefreshOccurrenceDataByKeyOccurrenceKey(String value) {
        return new JAXBElement<String>(_GetOccurrenceByKeyOccurrenceKey_QNAME, String.class, RefreshOccurrenceDataByKey.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EwaResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "GetAttachmentsDetailsResult", scope = GetAttachmentsDetailsResponse.class)
    public JAXBElement<EwaResult> createGetAttachmentsDetailsResponseGetAttachmentsDetailsResult(EwaResult value) {
        return new JAXBElement<EwaResult>(_GetAttachmentsDetailsResponseGetAttachmentsDetailsResult_QNAME, EwaResult.class, GetAttachmentsDetailsResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "userManagerToken", scope = GetImpersonatedUserCredentials.class)
    public JAXBElement<String> createGetImpersonatedUserCredentialsUserManagerToken(String value) {
        return new JAXBElement<String>(_GetUsersUserManagerToken_QNAME, String.class, GetImpersonatedUserCredentials.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "language", scope = GetImpersonatedUserCredentials.class)
    public JAXBElement<String> createGetImpersonatedUserCredentialsLanguage(String value) {
        return new JAXBElement<String>(_LoginLanguage_QNAME, String.class, GetImpersonatedUserCredentials.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "username", scope = GetImpersonatedUserCredentials.class)
    public JAXBElement<String> createGetImpersonatedUserCredentialsUsername(String value) {
        return new JAXBElement<String>(_LoginUsername_QNAME, String.class, GetImpersonatedUserCredentials.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "repository", scope = GetImpersonatedUserCredentials.class)
    public JAXBElement<String> createGetImpersonatedUserCredentialsRepository(String value) {
        return new JAXBElement<String>(_LoginRepository_QNAME, String.class, GetImpersonatedUserCredentials.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "userManagerToken", scope = SaveUser.class)
    public JAXBElement<String> createSaveUserUserManagerToken(String value) {
        return new JAXBElement<String>(_GetUsersUserManagerToken_QNAME, String.class, SaveUser.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "user", scope = SaveUser.class)
    public JAXBElement<String> createSaveUserUser(String value) {
        return new JAXBElement<String>(_SaveUserUser_QNAME, String.class, SaveUser.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EwaResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "GetValuesResult", scope = GetValuesResponse.class)
    public JAXBElement<EwaResult> createGetValuesResponseGetValuesResult(EwaResult value) {
        return new JAXBElement<EwaResult>(_GetValuesResponseGetValuesResult_QNAME, EwaResult.class, GetValuesResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EwaResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "GetUsersResult", scope = GetUsersResponse.class)
    public JAXBElement<EwaResult> createGetUsersResponseGetUsersResult(EwaResult value) {
        return new JAXBElement<EwaResult>(_GetUsersResponseGetUsersResult_QNAME, EwaResult.class, GetUsersResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "loginInfo", scope = LoginByInfo.class)
    public JAXBElement<String> createLoginByInfoLoginInfo(String value) {
        return new JAXBElement<String>(_UserManagerLoginByInfoLoginInfo_QNAME, String.class, LoginByInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "userToken", scope = ExecuteQueryCount.class)
    public JAXBElement<String> createExecuteQueryCountUserToken(String value) {
        return new JAXBElement<String>(_GetQueryObjectUserToken_QNAME, String.class, ExecuteQueryCount.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "libraryName", scope = ExecuteQueryCount.class)
    public JAXBElement<String> createExecuteQueryCountLibraryName(String value) {
        return new JAXBElement<String>(_GetQueryObjectLibraryName_QNAME, String.class, ExecuteQueryCount.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "queryName", scope = ExecuteQueryCount.class)
    public JAXBElement<String> createExecuteQueryCountQueryName(String value) {
        return new JAXBElement<String>(_GetQueryObjectQueryName_QNAME, String.class, ExecuteQueryCount.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "userToken", scope = ExecuteQueryObjectAsTable.class)
    public JAXBElement<String> createExecuteQueryObjectAsTableUserToken(String value) {
        return new JAXBElement<String>(_GetQueryObjectUserToken_QNAME, String.class, ExecuteQueryObjectAsTable.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "queryObject", scope = ExecuteQueryObjectAsTable.class)
    public JAXBElement<String> createExecuteQueryObjectAsTableQueryObject(String value) {
        return new JAXBElement<String>(_ExecuteQueryObjectQueryObject_QNAME, String.class, ExecuteQueryObjectAsTable.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EwaResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "GetUserResult", scope = GetUserResponse.class)
    public JAXBElement<EwaResult> createGetUserResponseGetUserResult(EwaResult value) {
        return new JAXBElement<EwaResult>(_GetUserResponseGetUserResult_QNAME, EwaResult.class, GetUserResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EwaResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "ExecuteQueryObjectCountResult", scope = ExecuteQueryObjectCountResponse.class)
    public JAXBElement<EwaResult> createExecuteQueryObjectCountResponseExecuteQueryObjectCountResult(EwaResult value) {
        return new JAXBElement<EwaResult>(_ExecuteQueryObjectCountResponseExecuteQueryObjectCountResult_QNAME, EwaResult.class, ExecuteQueryObjectCountResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "userToken", scope = GetAttachmentsDetailsByKey.class)
    public JAXBElement<String> createGetAttachmentsDetailsByKeyUserToken(String value) {
        return new JAXBElement<String>(_GetQueryObjectUserToken_QNAME, String.class, GetAttachmentsDetailsByKey.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "occurrenceKey", scope = GetAttachmentsDetailsByKey.class)
    public JAXBElement<String> createGetAttachmentsDetailsByKeyOccurrenceKey(String value) {
        return new JAXBElement<String>(_GetOccurrenceByKeyOccurrenceKey_QNAME, String.class, GetAttachmentsDetailsByKey.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EwaResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "IsValidTokenResult", scope = IsValidTokenResponse.class)
    public JAXBElement<EwaResult> createIsValidTokenResponseIsValidTokenResult(EwaResult value) {
        return new JAXBElement<EwaResult>(_IsValidTokenResponseIsValidTokenResult_QNAME, EwaResult.class, IsValidTokenResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "userToken", scope = GetEntities.class)
    public JAXBElement<String> createGetEntitiesUserToken(String value) {
        return new JAXBElement<String>(_GetQueryObjectUserToken_QNAME, String.class, GetEntities.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EwaResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "IsValidQueryResultResult", scope = IsValidQueryResultResponse.class)
    public JAXBElement<EwaResult> createIsValidQueryResultResponseIsValidQueryResultResult(EwaResult value) {
        return new JAXBElement<EwaResult>(_IsValidQueryResultResponseIsValidQueryResultResult_QNAME, EwaResult.class, IsValidQueryResultResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "userToken", scope = ExecuteQueryObjectCount.class)
    public JAXBElement<String> createExecuteQueryObjectCountUserToken(String value) {
        return new JAXBElement<String>(_GetQueryObjectUserToken_QNAME, String.class, ExecuteQueryObjectCount.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "queryObject", scope = ExecuteQueryObjectCount.class)
    public JAXBElement<String> createExecuteQueryObjectCountQueryObject(String value) {
        return new JAXBElement<String>(_ExecuteQueryObjectQueryObject_QNAME, String.class, ExecuteQueryObjectCount.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "userToken", scope = GetAttribute.class)
    public JAXBElement<String> createGetAttributeUserToken(String value) {
        return new JAXBElement<String>(_GetQueryObjectUserToken_QNAME, String.class, GetAttribute.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "userManagerToken", scope = GetUser.class)
    public JAXBElement<String> createGetUserUserManagerToken(String value) {
        return new JAXBElement<String>(_GetUsersUserManagerToken_QNAME, String.class, GetUser.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "username", scope = GetUser.class)
    public JAXBElement<String> createGetUserUsername(String value) {
        return new JAXBElement<String>(_LoginUsername_QNAME, String.class, GetUser.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EwaResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "LoginResult", scope = LoginResponse.class)
    public JAXBElement<EwaResult> createLoginResponseLoginResult(EwaResult value) {
        return new JAXBElement<EwaResult>(_LoginResponseLoginResult_QNAME, EwaResult.class, LoginResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EwaResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "GetAttributeValueResult", scope = GetAttributeValueResponse.class)
    public JAXBElement<EwaResult> createGetAttributeValueResponseGetAttributeValueResult(EwaResult value) {
        return new JAXBElement<EwaResult>(_GetAttributeValueResponseGetAttributeValueResult_QNAME, EwaResult.class, GetAttributeValueResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "password", scope = UserManagerLogin.class)
    public JAXBElement<String> createUserManagerLoginPassword(String value) {
        return new JAXBElement<String>(_LoginPassword_QNAME, String.class, UserManagerLogin.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "username", scope = UserManagerLogin.class)
    public JAXBElement<String> createUserManagerLoginUsername(String value) {
        return new JAXBElement<String>(_LoginUsername_QNAME, String.class, UserManagerLogin.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "repository", scope = UserManagerLogin.class)
    public JAXBElement<String> createUserManagerLoginRepository(String value) {
        return new JAXBElement<String>(_LoginRepository_QNAME, String.class, UserManagerLogin.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "userToken", scope = GetValues.class)
    public JAXBElement<String> createGetValuesUserToken(String value) {
        return new JAXBElement<String>(_GetQueryObjectUserToken_QNAME, String.class, GetValues.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "filter", scope = GetValues.class)
    public JAXBElement<String> createGetValuesFilter(String value) {
        return new JAXBElement<String>(_GetValuesFilter_QNAME, String.class, GetValues.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EwaResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "SaveUserInfoRawResult", scope = SaveUserInfoRawResponse.class)
    public JAXBElement<EwaResult> createSaveUserInfoRawResponseSaveUserInfoRawResult(EwaResult value) {
        return new JAXBElement<EwaResult>(_SaveUserInfoRawResponseSaveUserInfoRawResult_QNAME, EwaResult.class, SaveUserInfoRawResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EwaResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "GetExecutedQueryResult", scope = GetExecutedQueryResponse.class)
    public JAXBElement<EwaResult> createGetExecutedQueryResponseGetExecutedQueryResult(EwaResult value) {
        return new JAXBElement<EwaResult>(_GetExecutedQueryResponseGetExecutedQueryResult_QNAME, EwaResult.class, GetExecutedQueryResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "userToken", scope = SaveUserInfo.class)
    public JAXBElement<String> createSaveUserInfoUserToken(String value) {
        return new JAXBElement<String>(_GetQueryObjectUserToken_QNAME, String.class, SaveUserInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "userInfo", scope = SaveUserInfo.class)
    public JAXBElement<String> createSaveUserInfoUserInfo(String value) {
        return new JAXBElement<String>(_SaveUserInfoUserInfo_QNAME, String.class, SaveUserInfo.class, value);
    }

}
