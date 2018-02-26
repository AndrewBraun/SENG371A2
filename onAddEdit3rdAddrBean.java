package billingPackage;
import java.io.*;
import javax.servlet.jsp.*;
import javax.servlet.http.*;
import java.util.*;
import java.sql.*;
import oscar.*;
import java.text.*;
import java.net.*;

public class onAddEdit3rdAddrBean implements java.io.Serializable{

    private HttpServletRequest request;
    private JspWriter out;
    private Properties prop;
    private Properties val;
    private String msg = "Type in a name and search first to see if it is available.";
    private String action = "search"; // add/edit
    private String requestSubmit;
    private boolean authed;

    public void init( HttpServletRequest request, JspWriter out){
        this.request=request;
        this.out=out;
        prop = new Properties();
        val = new Properties();
    }

    public void setMsg (String message){
        msg = message;
    }

    public String getMsg() {
        return msg;
    }

    public void setAction(String message){
        action = message;
    }

    public String getAction() {
        return action;
    }

    public void setRequestSubmit(){
        requestSubmit = request.getParameter("submit");
    }

    public String getRequestSubmit() {
        return requestSubmit;
    }

    public void setAuthed(boolean value){
        authed=value;
    }

    public boolean getAuthed() {
        return authed;
    }

    public void checkSessionForLogout() throws IOException{
        if (session.getAttribute("user") == null) {
            response.sendRedirect("../logout.jsp");
        }
        String user_no = (String) session.getAttribute("user");
    }

    public void setValProperties(){
        val.setProperty("id", request.getParameter("id"));
        val.setProperty("attention", request.getParameter("attention"));
        val.setProperty("company_name", request.getParameter("company_name"));
        val.setProperty("address", request.getParameter("address"));
        val.setProperty("city", request.getParameter("city"));
        val.setProperty("province", request.getParameter("province"));
        val.setProperty("postcode", request.getParameter("postcode"));
        val.setProperty("telephone", request.getParameter("telephone"));
        val.setProperty("fax", request.getParameter("fax"));
    }

    public void updateServiceCode(){
        String company_name = request.getParameter("company_name");
        if (company_name.equals(request.getParameter("action").substring("edit".length()))) {
            String list = "";
            setValProperties();

            boolean ni = dbObj.update3rdAddr(request.getParameter("id"), val);
            if (ni) {
                setMsg(company_name + " is updated.<br>"
                        + "Type in a name and search first to see if it is available.");
                setAction("search");
                prop.setProperty("company_name", company_name);
            } else {
                setMsg(company_name + " is <font color='red'>NOT</font> updated. Action failed! Try edit it again.");
                setAction("edit" + company_name);
                prop.setProperty("company_name", company_name);
                prop.setProperty("id", request.getParameter("id"));
                prop.setProperty("attention", request.getParameter("attention"));
                prop.setProperty("company_name", request.getParameter("company_name"));
                prop.setProperty("address", request.getParameter("address"));
                prop.setProperty("city", request.getParameter("city"));
                prop.setProperty("province", request.getParameter("province"));
                prop.setProperty("postcode", request.getParameter("postcode"));
                prop.setProperty("telephone", request.getParameter("telephone"));
                prop.setProperty("fax", request.getParameter("fax"));
            }
        } else {
            setMsg("You can <font color='red'>NOT</font> save the name - " + company_name
                    + ". Please search the name first.");
            setAction("search");
            prop.setProperty("company_name", company_name);
        }
    }

    public void addServiceCode(){
        String company_name = request.getParameter("company_name");
        if (company_name.equals(request.getParameter("action").substring("add".length()))) {
            val.setProperty("attention", request.getParameter("attention"));
            val.setProperty("company_name", request.getParameter("company_name"));
            val.setProperty("address", request.getParameter("address"));
            val.setProperty("city", request.getParameter("city"));
            val.setProperty("province", request.getParameter("province"));
            val.setProperty("postcode", request.getParameter("postcode"));
            val.setProperty("telephone", request.getParameter("telephone"));
            val.setProperty("fax", request.getParameter("fax"));
            int ni = dbObj.addOne3rdAddrRecord(val);
            if (ni > 0) {
                setMsg(company_name + " is added.<br>"
                        + "Type in a name and search first to see if it is available.");
                setAction("search");
                prop.setProperty("company_name", company_name);
            } else {
                setMsg(company_name + " is <font color='red'>NOT</font> added. Action failed! Try edit it again.");
                setAction("add" + company_name);
                prop.setProperty("company_name", company_name);
                prop.setProperty("attention", request.getParameter("attention"));
                prop.setProperty("company_name", request.getParameter("company_name"));
                prop.setProperty("address", request.getParameter("address"));
                prop.setProperty("city", request.getParameter("city"));
                prop.setProperty("province", request.getParameter("province"));
                prop.setProperty("postcode", request.getParameter("postcode"));
                prop.setProperty("telephone", request.getParameter("telephone"));
                prop.setProperty("fax", request.getParameter("fax"));
            }
        } else {
            setMsg("You can <font color='red'>NOT</font> save the name - " + company_name
                    + ". Please search the name first.");
            setAction("search");
            prop.setProperty("company_name", company_name);
        }
    }

    public void handleRequestSubmit() throws IOExecution{

        int serviceCodeLen = 5;
        //BillingServiceCode serviceCodeObj = new BillingServiceCode();
        JdbcBilling3rdPartImpl dbObj = new JdbcBilling3rdPartImpl();
        setRequestSubmit();
        if (getRequestSubmit() == null) return;
        if (getRequestSubmit().equals("Save")) {
            if (request.getParameter("action").startsWith("edit")) {
                updateServiceCode();
            } else if (request.getParameter("action").startsWith("add")) {
                addServiceCode();
            } else {
                setMsg("You can <font color='red'>NOT</font> save the name. Please search the name first.");
            }
        } else if (getRequestSubmit().equals("Search")) {
            // check the input data
            if (request.getParameter("company_name") == null) {
                setMsg("Please type in a right name.");
            } else {
                String company_name = request.getParameter("company_name");
                Properties ni = dbObj.get3rdAddrProp(company_name);
                if (!ni.getProperty("company_name", "").equals("")) {
                    prop = ni; //.setProperty("company_name", company_name);
                    int n = 0;
                    setMsg("You can edit the name.");
                    setAction("edit" + company_name);

                } else {
                    prop.setProperty("company_name", company_name);
                    setMsg("It is a NEW name. You can add it.");
                    setAction("add" + company_name);
                }
            }
        }
    }

}
