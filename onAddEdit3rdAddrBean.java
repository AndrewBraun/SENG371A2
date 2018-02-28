package oscar;

import java.util.*;
import java.sql.*;
import java.text.*;
import java.net.*;
import java.io.Serializable;

public class onAddEdit3rdAddrBean implements Serializable{

    private boolean testBoolean = true;

    public onAddEdit3rdAddrBean() {}

    public void init (){
    }

    public void setTestBoolean(boolean value){
        testBoolean = value;
    }

    public boolean isTestBoolean() {
        return testBoolean;
    }
}
