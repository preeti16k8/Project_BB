package com.kre8tives.bareboneneww.Model;

/**
 * Created by user on 6/12/2017.
 */

public class NewCurrentOrder {
    public NewCurrentOrder(String cid, String cname, String cqty, String cprice) {
        this.cid = cid;
        this.cname = cname;
        this.cqty = cqty;
        this.cprice = cprice;
    }

    public NewCurrentOrder(String cid, String cname, String cqty, String cprice, String cstatus) {
        this.cid = cid;
        this.cname = cname;
        this.cqty = cqty;
        this.cprice = cprice;
        this.cstatus = cstatus;
    }

    public String getCstatus() {
        return cstatus;
    }

    public void setCstatus(String cstatus) {
        this.cstatus = cstatus;
    }

    public NewCurrentOrder() {

    }

    String cid;
    String cname;
    String cqty;
    String cprice;
    String cstatus;

    public String getCsubtotal() {
        return csubtotal;
    }

    public void setCsubtotal(String csubtotal) {
        this.csubtotal = csubtotal;
    }

    String csubtotal;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getCqty() {
        return cqty;
    }

    public void setCqty(String cqty) {
        this.cqty = cqty;
    }

    public String getCprice() {
        return cprice;
    }

    public void setCprice(String cprice) {
        this.cprice = cprice;
    }
}
