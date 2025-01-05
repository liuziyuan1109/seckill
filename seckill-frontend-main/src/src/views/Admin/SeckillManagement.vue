<template>
  <div class="seckill-management">
    <el-card>
      <el-row>
        <el-button type="primary" @click="showCreateForm">创建秒杀活动</el-button>
      </el-row>
      <el-table :data="activities" border style="margin-top: 20px">
        <el-table-column prop="activity_name" label="活动名称"></el-table-column>
        <el-table-column prop="startDate" label="开始时间"></el-table-column>
        <el-table-column prop="endDate" label="结束时间"></el-table-column>
        <el-table-column prop="status" label="状态"></el-table-column>
        <el-table-column label="操作">
          <template #default="{ row }">
            <el-button size="mini" @click="editActivity(row)">编辑</el-button>
            <el-button size="mini" type="danger" @click="confirmDeleteActivity(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 弹窗表单 -->
    <el-dialog :visible.sync="showForm" title="秒杀活动">
      <SeckillForm ref="seckillForm" :activity="selectedActivity" @submit="handleFormSubmit" />
    </el-dialog>
  </div>
</template>

<script>
import SeckillForm from "../components/SeckillForm.vue";

const API_BASE_URL = process.env.VUE_APP_API_BASE_URL;

export default {
  name: "SeckillManagement",
  components: {
    SeckillForm,
  },
  data() {
    return {
      activities: [],
      showForm: false,
      selectedActivity: null,
    };
  },
  methods: {
    async fetchActivities() {
      try {
        const response = await this.$axios.get(`${API_BASE_URL}/api/admin/seckill-products`);
        this.activities = response.map((activity) => ({
          ...activity,
          status: this.getActivityStatus(activity),
        }));
      } catch (error) {
        console.error('获取秒杀活动列表失败:', error.message);
        this.$message.error('加载活动列表失败');
      }
    },
    getActivityStatus(activity) {
      const now = new Date();
      const startDate = new Date(activity.startDate);
      const endDate = new Date(activity.endDate);
      if (now < startDate) {
        return "未开始";
      } else if (now >= startDate && now <= endDate) {
        return "进行中";
      } else {
        return "已结束";
      }
    },
    showCreateForm() {
      this.selectedActivity = null;
      this.$nextTick(() => {
        this.$refs.seckillForm.resetForm();
      });
      this.showForm = true;
    },
    editActivity(activity) {
      this.selectedActivity = activity;
      this.$nextTick(() => {
        this.$refs.seckillForm.resetForm();
      });
      this.showForm = true;
    },
    confirmDeleteActivity(id) {
      this.$confirm("确认删除该活动吗？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.deleteActivity(id);
        })
        .catch(() => {
          this.$message.info("已取消删除");
        });
    },
    async deleteActivity(id) {
      await this.$axios.delete(`${API_BASE_URL}/api/admin/seckill-products/${id}`);
      this.$message.success("删除成功");
      this.fetchActivities();
    },
    async handleFormSubmit() {
      this.fetchActivities();
      this.showForm = false;
      this.$message.success(this.selectedActivity ? "更新成功" : "创建成功");
    },
  },
  created() {
    this.fetchActivities();
    // 定时刷新状态
    setInterval(this.fetchActivities, 60000); // 每分钟刷新一次
  },
};
</script>
