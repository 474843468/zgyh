package com.boc.bocsoft.mobile.common.utils.BeanConvertor;





import java.util.List;



/**
 * Created by dzq on 15/8/25.
 */
public abstract class Convertor {

    public Convertor() {
    }

    private Transformer[] transformers;

    private List supportClasses;

    protected Transformer[] getTransformerList() {
        return transformers;
    }

    public void setTransformerList(Transformer... transformers) {
        this.transformers = transformers;
        for (Transformer transformer : transformers) {
            supportClasses.add(transformer.getSerializedType());
        }
    }
}
