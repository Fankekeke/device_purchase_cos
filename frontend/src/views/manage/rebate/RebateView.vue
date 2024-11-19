<template>
  <a-modal v-model="show" title="商品折扣设置详情" @cancel="onClose" :width="1000">
    <template slot="footer">
      <a-button key="back" @click="onClose" rebate="danger">
        关闭
      </a-button>
    </template>
    <div style="font-size: 13px;font-family: SimHei" v-if="rebateData !== null">
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">商品信息</span></a-col>
        <a-col :span="8"><b>商品名称：</b>
          {{ rebateData.commodityName }}
        </a-col>
        <a-col :span="8"><b>商品型号：</b>
          {{ rebateData.model }}
        </a-col>
        <a-col :span="8"><b>商品类型：</b>
          {{ rebateData.typeName }}
        </a-col>
        <br/>
        <br/>
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">商品图片</span></a-col>
        <a-col :span="24">
          <a-upload
            name="avatar"
            action="http://127.0.0.1:9527/file/fileUpload/"
            list-type="picture-card"
            :file-list="fileList"
            @preview="handlePreview"
            @change="picHandleChange">
          </a-upload>
          <a-modal :visible="previewVisible" :footer="null" @cancel="handleCancel">
            <img alt="example" style="width: 100%" :src="previewImage" />
          </a-modal>
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">折扣信息</span></a-col>
        <a-col :span="8"><b>底价折扣：</b>
          {{ rebateData.lowRate }}
        </a-col>
        <a-col :span="8"><b>正常价格：</b>
          {{ rebateData.specialPrice }}
        </a-col>
        <a-col :span="8"><b>商品类型：</b>
          {{ rebateData.normalPrice }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>底价限制：</b>
          {{ rebateData.lowRestriction ? rebateData.lowRestriction : '无'}}
        </a-col>
        <a-col :span="8"><b>特价限制：</b>
          {{ rebateData.specialRestriction ? rebateData.specialRestriction : '无'}}
        </a-col>
        <a-col :span="8"><b>创建时间：</b>
          {{ rebateData.createDate }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">备注</span></a-col>
        <a-col :span="24">
          {{ rebateData.remark ? rebateData.remark : '无'}}
        </a-col>
      </a-row>
      <br/>
    </div>
  </a-modal>
</template>

<script>
import moment from 'moment'
moment.locale('zh-cn')
function getBase64 (file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.readAsDataURL(file)
    reader.onload = () => resolve(reader.result)
    reader.onerror = error => reject(error)
  })
}
export default {
  name: 'rebateView',
  props: {
    rebateShow: {
      rebate: Boolean,
      default: false
    },
    rebateData: {
      rebate: Object
    }
  },
  computed: {
    show: {
      get: function () {
        return this.rebateShow
      },
      set: function () {
      }
    }
  },
  data () {
    return {
      loading: false,
      fileList: [],
      previewVisible: false,
      previewImage: '',
      durgList: []
    }
  },
  watch: {
    rebateShow: function (value) {
      if (value) {
        this.imagesInit(this.rebateData.images)
      }
    }
  },
  methods: {
    imagesInit (images) {
      if (images !== null && images !== '') {
        let imageList = []
        images.split(',').forEach((image, index) => {
          imageList.push({uid: index, name: image, status: 'done', url: 'http://127.0.0.1:9527/imagesWeb/' + image})
        })
        this.fileList = imageList
      }
    },
    handleCancel () {
      this.previewVisible = false
    },
    async handlePreview (file) {
      if (!file.url && !file.preview) {
        file.preview = await getBase64(file.originFileObj)
      }
      this.previewImage = file.url || file.preview
      this.previewVisible = true
    },
    picHandleChange ({ fileList }) {
      this.fileList = fileList
    },
    onClose () {
      this.$emit('close')
    }
  }
}
</script>

<style scoped>

</style>
