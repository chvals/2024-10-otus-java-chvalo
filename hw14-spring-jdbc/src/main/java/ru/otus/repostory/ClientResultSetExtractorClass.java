package ru.otus.repostory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.otus.domain.Address;
import ru.otus.domain.Client;
import ru.otus.domain.Phone;

public class ClientResultSetExtractorClass implements ResultSetExtractor<List<Client>> {
    @Override
    public List<Client> extractData(ResultSet rs) throws SQLException, DataAccessException {
        var clientList = new ArrayList<Client>();
        long prevClientId = 0;
        while (rs.next()) {
            Client client = null;
            var clientid = rs.getLong("clientid");
            if (prevClientId == 0 || prevClientId != clientid) {
                Address address = new Address(rs.getLong("addressid"), clientid, rs.getString("street"));
                Phone phone = new Phone(rs.getLong("phoneid"), clientid, rs.getString("number"));
                client = new Client(clientid, rs.getString("name"), address, Set.of(phone), false);
                clientList.add(client);
                prevClientId = clientid;
            }
        }
        return clientList;
    }
}
