<template>
  <el-form :model="form" :rules="rules" ref="form" label-width="120px">
    <el-form-item label="活动名称" prop="name">
      <el-input v-model="form.name" />
    </el-form-item>
    <el-form-item label="开始时间" prop="startTime">
      <el-date-picker v-model="form.startTime" type="datetime" />
    </el-form-item>
    <el-form-item label="结束时间" prop="endTime">
      <el-date-picker v-model="form.endTime" type="datetime" />
    </el-form-item>
    <el-form-item label="活动商品" prop="productId">
      <el-select v-model="form.productId" placeholder="选择商品">
        <el-option v-for="item in products" :key="item.id" :label="item.name" :value="item.id" />
      </el-select>
    </el-form-item>
    <el-form-item label="秒杀价格" prop="price">
      <el-input-number v-model="form.price" />
    </el-form-item>
    <el-form-item label="库存" prop="stock">
      <el-input-number v-model="form.stock" />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="submit">提交</el-button>
      <el-button @click="$emit('close')">取消</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
const API_BASE_URL = process.env.VUE_APP_API_BASE_URL;
export default {
  props: {
    activity: {
      type: Object,
      default: () => ({}),
    },
  },
  data() {
    return {
      form: {
        id: null,
        name: "",
        startTime: "",
        endTime: "",
        productId: null,
        price: 0,
        stock: 0,
      },
      products: [],
      rules: {
        name: [{ required: true, message: "请输入活动名称", trigger: "blur" }],
        startTime: [{ required: true, message: "请选择开始时间", trigger: "change" }],
        endTime: [{ required: true, message: "请选择结束时间", trigger: "change" }],
        productId: [{ required: true, message: "请选择商品", trigger: "change" }],
        price: [{ required: true, type: "number", message: "请输入价格", trigger: "blur" }],
        stock: [{ required: true, type: "number", message: "请输入库存", trigger: "blur" }],
      },
    };
  },
  watch: {
    activity: {
      immediate: true,
      handler(newVal) {
        this.form = { ...this.form, ...newVal };
      },
    },
  },
  methods: {
    resetForm() {
      this.form = {
        id: null,
        name: "",
        startTime: "",
        endTime: "",
        productId: null,
        price: 0,
        stock: 0,
      };
    },
    async fetchProducts() {
      const response = await this.$axios.get(`${API_BASE_URL}/api/admin/seckill-products/products`);
      this.products = response;
      console.log("商品列表：", this.products);
    },
    async submit() {
  this.$refs.form.validate((valid) => {
    if (!valid) return;

    console.log(this.form);
    const payload = {
      id: this.form.id,
      activity_name: this.form.name,
      goodsId: this.form.productId,
      seckillPrice: this.form.price,
      stockCount: this.form.stock,
      startDate: this.form.startTime
        ? `${this.form.startTime.getFullYear()}-${String(this.form.startTime.getMonth() + 1).padStart(2, "0")}-${String(this.form.startTime.getDate()).padStart(2, "0")}T${String(this.form.startTime.getHours()).padStart(2, "0")}:${String(this.form.startTime.getMinutes()).padStart(2, "0")}:${String(this.form.startTime.getSeconds()).padStart(2, "0")}`
        : null,
      endDate: this.form.endTime
        ? `${this.form.endTime.getFullYear()}-${String(this.form.endTime.getMonth() + 1).padStart(2, "0")}-${String(this.form.endTime.getDate()).padStart(2, "0")}T${String(this.form.endTime.getHours()).padStart(2, "0")}:${String(this.form.endTime.getMinutes()).padStart(2, "0")}:${String(this.form.endTime.getSeconds()).padStart(2, "0")}`
        : null,
    };

    console.log("提交的数据：", payload);

    if (this.form.id) {
      this.$axios
        .put(`${API_BASE_URL}/api/admin/seckill-products/${this.form.id}`, payload)
        .then((response) => {
          console.log("更新成功：", response.data);
          this.$emit("submit");
        })
        .catch((error) => {
          console.error("更新失败：", error);
        });
    } else {
      this.$axios
        .post(`${API_BASE_URL}/api/admin/seckill-products`, payload)
        .then((response) => {
          console.log("创建成功：", response);
          this.$emit("submit");
        })
        .catch((error) => {
          console.error("创建失败：", error);
        });
    }
  });
}

  },
  created() {
    this.fetchProducts();
  },
};
</script>