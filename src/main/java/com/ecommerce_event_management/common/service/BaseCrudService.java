package com.ecommerce_event_management.common.service;

import com.ecommerce_event_management.common.dto.CommonFilterDTO;
import com.ecommerce_event_management.common.dto.PageResponse;

/**
 * BaseCrudService: Generic CRUD contract for all future business entities.
 *
 * @param <RESP> Response DTO type (extends BaseDTO)
 * @param <REQ>  Request DTO type (contains validation annotations)
 * @param <ID>   Primary Key type (typically Long)
 */
public interface BaseCrudService<RESP, REQ, ID> {

    PageResponse<RESP> getAll(CommonFilterDTO filter);

    RESP getById(ID id);

    RESP create(REQ request);

    RESP update(ID id, REQ request);

    void softDelete(ID id);

    void hardDelete(ID id);
}
