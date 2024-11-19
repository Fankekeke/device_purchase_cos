package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.StoreRecordInfo;
import cc.mrbird.febs.cos.service.IStoreRecordInfoService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 库房记录 控制层
 *
 * @author FanK wechat Storm_Berserker email fan1ke2ke@gmail.com
 */
@RestController
@RequestMapping("/cos/store-record-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StoreRecordInfoController {

    private final IStoreRecordInfoService storeRecordInfoService;

    /**
     * 分页获取库房记录信息
     *
     * @param page            分页对象
     * @param storeRecordInfo 库房记录信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<StoreRecordInfo> page, StoreRecordInfo storeRecordInfo) {
        return R.ok(storeRecordInfoService.selectStoreRecordPage(page, storeRecordInfo));
    }

    /**
     * 分页获取库房库存
     *
     * @param page            分页对象
     * @param storeRecordInfo 库房记录信息
     * @return 结果
     */
    @GetMapping("/stock/page")
    public R selectStorePage(Page<StoreRecordInfo> page, StoreRecordInfo storeRecordInfo) {
        return R.ok(storeRecordInfoService.selectStorePage(page, storeRecordInfo));
    }

    /**
     * 根据商品编号获取出库入库记录
     *
     * @param commodityCode 商品编号
     * @return 结果
     */
    @GetMapping("/selectOutInDetail")
    public R selectOutInDetail(String commodityCode) {
        return R.ok(storeRecordInfoService.selectOutInDetail(commodityCode));
    }

    /**
     * 查询库房记录信息详情
     *
     * @param id 主键ID
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(storeRecordInfoService.getById(id));
    }

    /**
     * 查询库房记录信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(storeRecordInfoService.list());
    }

    /**
     * 新增库房记录信息
     *
     * @param storeRecordInfo 库房记录信息
     * @return 结果
     */
    @PostMapping
    public R save(StoreRecordInfo storeRecordInfo) {
        return R.ok(storeRecordInfoService.save(storeRecordInfo));
    }

    /**
     * 修改库房记录信息
     *
     * @param storeRecordInfo 库房记录信息
     * @return 结果
     */
    @PutMapping
    public R edit(StoreRecordInfo storeRecordInfo) {
        return R.ok(storeRecordInfoService.updateById(storeRecordInfo));
    }

    /**
     * 删除库房记录信息
     *
     * @param ids ids
     * @return 库房记录信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(storeRecordInfoService.removeByIds(ids));
    }
}
