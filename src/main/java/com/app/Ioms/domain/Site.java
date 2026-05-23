package com.app.Ioms.domain;

public class Site {
    private final String code;
    private final String name;
    private final int shippingPriority; // nhỏ hơn = ưu tiên cao hơn

    public Site(String code, String name, int shippingPriority) {
        this.code = code;
        this.name = name;
        this.shippingPriority = shippingPriority;
    }

    public String getCode() {
        return code;
    }
package com.app.Ioms.domain;

public class Site {
    private final String code;
    private final String name;
    private final int shippingPriority;

    public Site(String code, String name, int shippingPriority) {
        this.code = code;
        this.name = name;
        this.shippingPriority = shippingPriority;
    }

    public String getCode() { return code; }
    public String getName() { return name; }
    public int getShippingPriority() { return shippingPriority; }

    @Override
    public String toString() {
        return name + " (" + code + ")";
    }
}
    public String getName() {
        return name;
    }

    public int getShippingPriority() {
        return shippingPriority;
    }

    @Override
    public String toString() {
        return name + " (" + code + ")";
    }
}
