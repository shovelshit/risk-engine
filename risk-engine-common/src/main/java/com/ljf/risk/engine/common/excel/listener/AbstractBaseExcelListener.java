package com.ljf.risk.engine.common.excel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelAnalysisException;
import com.alibaba.excel.metadata.Cell;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.ljf.risk.engine.common.validation.utils.ValidationResult;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * @author lijinfeng
 *
 * 如果默认实现的方法不满足业务,则直接自定义一个listener实现AnalysisEventListener,重写一遍方法即可
 */
@Slf4j
public abstract class AbstractBaseExcelListener<T> extends AnalysisEventListener<T> {

    /**
     * 自定义用于暂时存储data。
     * 可以通过实例获取该值
     * 可以指定AnalysisEventListener的泛型来确定List的存储类型
     */
    @Getter
    private final List<T> data = new ArrayList<>();

    @Getter
    private int total = 0;

    @Getter
    @Setter
    private int successCount = 0;

    /**
     * 每隔N条存执行一次{@link AbstractBaseExcelListener#doService()}方法,
     * 如果是入库操作,可使用默认的3000条,然后清理list,方便内存回收
     */
    private int batchCount = 3000;

    /**
     * @param batchCount see batchCount
     * @return this
     * @see AbstractBaseExcelListener#batchCount
     */
    public void batchCount(int batchCount) {
        this.batchCount = batchCount;
    }

    /**
     * 读取时抛出异常是否继续读取.
     * true:跳过继续读取 , false:停止读取 , 默认true
     */
    private boolean continueAfterThrowing = true;

    /**
     * 设置抛出解析过程中抛出异常后是否跳过继续读取下一行
     *
     * @param continueAfterThrowing 解析过程中抛出异常后是否跳过继续读取下一行
     */
    public void continueAfterThrowing(boolean continueAfterThrowing) {
        this.continueAfterThrowing = continueAfterThrowing;
    }

    /**
     * 错误数据列表
     */
    private List<Object> errRowList = new ArrayList<>();

    /**
     * 获取错误的行号,以pojo的形式返回
     *
     * @return 错误的行号
     */
    public List<Object> getErrRowsList() {
        return errRowList;
    }

    public void addErrRow(List<T> rows) {
        errRowList.addAll(rows);
    }

    public void addErrRow(T row) {
        errRowList.add(row);
    }

    /**
     * 每解析一行会回调invoke()方法。
     * 如果当前行无数据,该方法不会执行,
     * 也就是说如果导入的的excel表无数据,该方法不会执行,
     * 不需要对上传的Excel表进行数据非空判断
     *
     * @param object  当前读取到的行数据对应的java模型对象
     * @param context 定义了获取读取excel相关属性的方法
     */
    @Override
    public void invoke(T object, AnalysisContext context) {
        log.info("解析到一条数据:{}", object);

        ValidationResult validationResult = validateBeforeAddData(object);
        if (validationResult.isHasErrors()) {
            throw new ExcelAnalysisException(MessageFormat.format("错误行号: {0}, 错误信息: {1}", context.readRowHolder().getRowIndex() + 1, validationResult.getMessage()));
        }

        // 数据存储到list，供批量处理，或后续自己业务逻辑处理。
        data.add(object);
        total += 1;

        //如果continueAfterThrowing 为false 时保证数据插入的原子性
        if (data.size() >= batchCount && continueAfterThrowing) {
            doService();
            data.clear();
        }

    }

    /**
     * 该方法用于对读取excel过程中对每一行的数据进行校验操作,
     * 如果不需要对每行数据进行校验,则直接返回true即可.
     *
     * @param object 读取到的数据对象
     * @return 校验是否通过 true:通过 ; false:不通过
     */
    public abstract ValidationResult validateBeforeAddData(T object);

    /**
     * 对暂存数据的业务逻辑方法 .
     * 相关逻辑可以在该方法体内编写, 例如入库.
     */
    public abstract boolean doService();

    /**
     * 解析监听器
     * 每个sheet解析结束会执行该方法
     *
     * @param context 定义了获取读取excel相关属性的方法
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        long startTime = System.currentTimeMillis();
        doService();
        log.info("current sheetNo read done, sheetNo : {}, success size: {}, error size: {}, cost: {}ms", getCurrentSheetNo(context), successCount, errRowList.size(), System.currentTimeMillis() - startTime);
        data.clear();//解析结束销毁不用的资源
    }

    /**
     * 在转换异常 获取其他异常下会调用本接口。抛出异常则停止读取。如果这里不抛出异常则继续读取下一行。
     * 如果不重写该方法,默认抛出异常,停止读取
     *
     * @param exception exception
     * @param context   context
     */
    @Override
    public void onException(Exception exception, AnalysisContext context) throws Exception {

        Integer sheetNo = getCurrentSheetNo(context);
        Integer rowIndex = context.readRowHolder().getRowIndex();
        Object currentRowAnalysisResult = context.readRowHolder().getCurrentRowAnalysisResult();
        log.warn("parse excel failed, sheetNo:{}, row: {}, data: {}, msg: {}", sheetNo, rowIndex, currentRowAnalysisResult, exception.getMessage());
        // 如果continueAfterThrowing为false,则直接将异常抛出
        if (!continueAfterThrowing) {
            throw exception;
        }
        if (currentRowAnalysisResult instanceof LinkedHashMap) {
            List<String> cell = new ArrayList<>();
            Map<Integer, Cell> cellMap = context.readRowHolder().getCellMap();
            Collection<Cell> values = cellMap.values();
            for (Cell value : values) {
                ReadCellData readCellData = (ReadCellData) value;
                cell.add(readCellData.getStringValue());
            }
            cell.add(exception.getCause().getMessage());
            errRowList.add(cell);
        } else {
            errRowList.add(currentRowAnalysisResult);
        }
    }

    /**
     * 获取当前读取的sheet no
     *
     * @param context 定义了获取读取excel相关属性的方法
     * @return current sheet no
     */
    private Integer getCurrentSheetNo(AnalysisContext context) {
        return context.readSheetHolder().getSheetNo();
    };

    public Integer getErrorRowSize() {
        return errRowList.size();
    }

}
