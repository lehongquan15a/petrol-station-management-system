package com.gasstation.managementsystem.repository.criteria;

import com.gasstation.managementsystem.entity.Receipt;
import com.gasstation.managementsystem.model.dto.receipt.ReceiptDTOFilter;
import com.gasstation.managementsystem.utils.QueryGenerateHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.HashMap;

@Repository
@RequiredArgsConstructor
public class ReceiptRepositoryCriteria {
    private final EntityManager em;

    public HashMap<String, Object> findAll(ReceiptDTOFilter filter) {
        em.createQuery("select r from Receipt r inner join Card c inner join c.customer cus inner join c.creator cre where 1 = 1 and cre.name");
        StringBuilder query = new StringBuilder("select r from Receipt r inner join Card c inner join c.customer cus inner join c.creator cre where 1 = 1");
        QueryGenerateHelper qHelper = new QueryGenerateHelper();
        qHelper.setQuery(query);
        qHelper.between("r.createdDate", 0l, filter.getCreatedDate(), "createdDate", filter.getCreatedDate())
                .between("r.amount", 0d, filter.getAmount(), "amount", filter.getAmount())
                .like("r.reason", "reason", filter.getReason())
                .like("r.card.id", "cardId", filter.getCardId())
                .like("cus.name", "customerName", filter.getCustomerName())
                .like("cus.phone", "customerPhone", filter.getCustomerPhone())
                .like("cre.name", "creatorName", filter.getCreatorName());
        String countQuery = qHelper.getQuery().toString().replace("select r", "select count(r.id)");
        Query countTotalQuery = em.createQuery(countQuery);
        qHelper.sort("e.id", "DESC");
        TypedQuery<Receipt> tQuery = em.createQuery(query.toString(), Receipt.class);
        return qHelper.paging(tQuery, countTotalQuery, filter.getPageIndex(), filter.getPageSize());
    }
}