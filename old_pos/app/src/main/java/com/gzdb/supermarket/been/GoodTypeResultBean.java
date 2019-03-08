package com.gzdb.supermarket.been;

import java.util.List;

/**
 * Created by nongyd on 17/5/28.
 */

public class GoodTypeResultBean{

        private List<GoodTypesBean> itemTypes;
        private List<GoodUnitBean> itemUnitList;

        public List<GoodTypesBean> getItemTypes() {
            return itemTypes;
        }

        public void setItemTypes(List<GoodTypesBean> itemTypes) {
            this.itemTypes = itemTypes;
        }

        public List<GoodUnitBean> getItemUnitList() {
            return itemUnitList;
        }

        public void setItemUnitList(List<GoodUnitBean> itemUnitList) {
            this.itemUnitList = itemUnitList;
        }

}
