package com.ljf.risk.engine.web.listener;

import com.ljf.risk.engine.biz.service.RoasterListService;
import com.ljf.risk.engine.common.entity.RoasterList;
import com.ljf.risk.engine.common.excel.listener.AbstractBaseExcelListener;
import com.ljf.risk.engine.common.validation.group.ImportGroup;
import com.ljf.risk.engine.common.validation.utils.ValidateUtils;
import com.ljf.risk.engine.common.validation.utils.ValidationResult;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;

import java.util.Date;

/**
 * @author lijinfeng
 */
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class RoasterListListener extends AbstractBaseExcelListener<RoasterList> {

    private Long roasterId;
    private String userName;

    private RoasterListService roasterListService;

    private Date expiredAt;

    @Override
    public ValidationResult validateBeforeAddData(RoasterList roasterListImportModel) {
        // 不校验 return new ValidationResult(false, null);
        ValidationResult validationResult = ValidateUtils.fastValidateEntity(roasterListImportModel, ImportGroup.class);
        roasterListImportModel.setErrorMsg(validationResult.getMessage());
        return validationResult;
    }

    @Override
    public boolean doService() {
        for (RoasterList roasterList : getData()) {
            Date date = new Date();
            roasterList.setCreateTime(date);
            roasterList.setUpdateTime(date);
            roasterList.setUpdateUser(userName);
            roasterList.setRoasterId(roasterId);
            roasterList.setExpiredAt(expiredAt);
            boolean success = false;
            try {
                success = roasterListService.save(roasterList);
            } catch (Exception e) {
                if (e instanceof DuplicateKeyException) {
                    log.warn("RoasterListListener Duplicate data: {}, ", roasterList, e);
                    roasterList.setErrorMsg("重复数据");
                } else {
                    log.error("RoasterListListener failed: {}, ", roasterList, e);
                    roasterList.setErrorMsg("未知错误");
                }
            } finally {
                if (!success) {
                    addErrRow(roasterList);
                } else {
                    setSuccessCount(getSuccessCount() + 1);
                }
            }
        }
        return true;
    }
}


