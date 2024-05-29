package com.akatsuki.pioms.specs.service;


import com.akatsuki.pioms.franchise.aggregate.DELIVERY_DATE;
import com.akatsuki.pioms.order.dto.OrderDTO;
import com.akatsuki.pioms.specs.dto.SpecsDTO;

import java.util.List;

public interface SpecsService {
    /**
     * <h1>Specs Service</h1>
     *     <br>
     * <h1>Admin Functions</h1>
     * <h2>1. get Specs List : 모든 명세서 반환</h2>
     * <h2>2. get Specs List By Admin Code : 관리자가 관리하고 있는 가맹점들의 모든 명세서 반환 </h2>
     * <h2>3. get Specs By Admin Code : 관리자가 관ㄹ하고 있는 가맹점들의 명세서 상세 정보 반환 </h2>
     * <br>
     * <h1> Franchise Owner Functions</h1>
     * <h2>1. get Specs List By FrOwner Code : 점주의 모든 명세서를 반환 </h2>
     * <h2>2. get Specs By FrOwner Code : 점주의 명세서 상세 정보 반환 </h2>
     * <br>
     *
     * <h1> INNER FUNCTIONS </h1>
     * <h2> 1. After accpet order: 주문 승인 후 명세서 생성</h2>
     * */

    List<SpecsDTO> getSpecsList();
    void afterAcceptOrder(OrderDTO orderDTO);
    List<SpecsDTO> getSpecsListByAdminCode(int adminCode);
    SpecsDTO getSpecsByAdminCode(int adminCode, int specsCode);
    List<SpecsDTO> getSpecsListByFrOwnerCode(int franchiseOwnerCode);
    SpecsDTO getSpecsByFranchiseCode(int franchiseOwnerCode, int specsId);
}
