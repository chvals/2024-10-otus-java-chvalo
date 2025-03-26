package ru.otus.repostory;

import java.util.List;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import ru.otus.domain.Client;

public interface ClientRepository extends ListCrudRepository<Client, Long> {

    @Override
    @Query(
            value =
                    """
                select c.clientid     as clientid,
                       c.name         as name,
                       a.addressid    as addressid,
                       a.street       as street,
                       p.phoneid      as phoneid,
                       p.number       as number
                from client c
                         left outer join address a
                                         on a.clientid = c.clientid
                         left outer join phone p
                                         on p.clientid = c.clientid
                order by c.clientid
                                                              """,
            resultSetExtractorClass = ClientResultSetExtractorClass.class)
    List<Client> findAll();
}
