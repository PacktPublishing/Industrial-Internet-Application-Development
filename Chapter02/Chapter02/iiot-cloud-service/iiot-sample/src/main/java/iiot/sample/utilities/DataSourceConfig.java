package iiot.sample.utilities;

/**
 * Created by 212568770 on 4/16/17.
 */
public class DataSourceConfig {
    private final String dataSourceUri;
    private final String[] tenants;
    private final String userName;
    private final String password;
    private final DataConnectionParms dataConnectionParms;

   public DataSourceConfig(String dataSourceUri, String[] tenants, String userName, String password, DataConnectionParms dataConnectionParms){
        this.dataSourceUri = dataSourceUri;
        this.tenants = tenants;
        this.userName = userName;
        this.password = password;
        this.dataConnectionParms = dataConnectionParms;
    }

    public String getDataSourceUri() {
        return dataSourceUri;
    }

    public String[] getTenants() {
        return tenants;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public DataConnectionParms getDataConnectionParms() {
        return dataConnectionParms;
    }

    @Override
    public String toString(){
        return String.format("%s %s %s %s ", this.dataSourceUri, this.userName, this.password, this.tenants);
    }
}
