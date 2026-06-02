package com.app.modules.sales.request.createrequest.factory;

import com.app.database.manager.DatabaseManager;
import com.app.modules.sales.request.createrequest.repository.ICreateRequestRepository;
import com.app.modules.sales.request.createrequest.repository.MySQLCreateRequestRepository;
import com.app.modules.sales.request.createrequest.repository.PostgreSQLCreateRequestRepository;

public class CreateRequestRepositoryFactory {
    private static ICreateRequestRepository instance;

    public static ICreateRequestRepository getRepository() {
        // 2. Kiểm tra nếu đã có instance rồi thì trả về luôn (Lazy Initialization)
        String dbName = DatabaseManager.getDatabaseName();
        if (instance == null) {
            if ("mysql".equals(dbName)) {
                instance = new MySQLCreateRequestRepository();
            } else if ("postgresql".equals(dbName)) {
                instance = new PostgreSQLCreateRequestRepository();
            }
        }
        return instance;
    }
}
