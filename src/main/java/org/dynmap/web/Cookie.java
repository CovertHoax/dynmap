package org.dynmap.web;

public class Cookie {
    private String name;
    private String value;
    private String comment;
    private String domain;
    private int maxage = -1;
    private String path;
    private boolean secure;
    private int version = 1;
    /**
     * Create cookie with given name and value
     * 
     * @param name
     * @param value
     */
    public Cookie(String name, String value) {
        this.name = name;
        this.value = value;
    }
    
    public Object clone() {
        Cookie c = new Cookie(name, value);
        c.comment = comment;
        c.domain = domain;
        c.maxage = maxage;
        c.path = path;
        c.secure = secure;
        c.version = version;
        
        return c;
    }
    /**
     * Get comment for cookie
     * @return
     */
    public String getComment() {
        return comment;
    }
    /**
     * Get domain for cookie
     * @return
     */
    public String getDomain() {
        return domain;
    }
    /**
     * Get max gge for cookie in seconds (default -1: persist until shutdown)
     * @return
     */
    public int getMaxAge() {
        return maxage;
    }
    /**
     * Get name of cookie
     * @return
     */
    public String getName() {
        return name;
    }
    /**
     * Get path on the server for the cookie
     * @return
     */
    public String getPath() {
        return path;
    }
    /**
     * Get secure cookie (secure protocol only)
     * @return
     */
    public boolean getSecure() {
        return secure;
    }
    /**
     * Get value of cookie
     * @return
     */
    public String getValue() {
        return value;
    }
    /**
     * Get cookie protocol version
     * @return
     */
    public int getVersion() {
        return version;
    }
    /**
     * Set comment for cookie
     * @param purpose
     */
    public void setComment(String purpose) {
        comment = purpose;
    }
    /**
     * Set domain for cookie
     * @param pattern
     */
    public void setDomain(String pattern) {
        domain = pattern;
    }
    /**
     * Set max age for cookie in seconds
     * @param expiry
     */
    public void setMaxAge(int expiry) {
        maxage = expiry;
    }
    /**
     * Set path for cookie
     * @param uri
     */
    public void setPath(String uri) {
        path = uri;
    }
    /**
     * Set secure flag
     * @param flag
     */
    public void setSecure(boolean flag) {
        secure = flag;
    }
    /**
     * Set value for cookie
     * @param newValue
     */
    public void setValue(String newval) {
        value = newval;
    }
    /**
     * Set cookie version value
     * @param v
     */
    public void setVersion(int ver) {
        version = ver;
    }
}
