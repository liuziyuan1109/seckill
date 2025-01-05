<template>
    <div class="seckill-list">
        <h1>秒杀专区</h1>

        <!-- 搜索和筛选区域 -->
        <div class="search-filter">
            <el-input placeholder="搜索商品" v-model="searchKeyword" @change="handleSearch"
                style="width: 300px; margin-right: 20px;">
                <i slot="prefix" class="el-icon-search"></i>
            </el-input>

            <el-input-number v-model="priceRange.min" @change="handleFilter" :min="0" placeholder="最低价"
                style="margin-right: 10px;">
            </el-input-number>
            <span>-</span>
            <el-input-number v-model="priceRange.max" @change="handleFilter" :min="0" placeholder="最高价"
                style="margin-left: 10px;">
            </el-input-number>

            <el-button @click="resetFilter" type="primary" style="margin-left: 20px;">重置筛选</el-button>
            <!-- 普通商城按钮 -->
            <el-button @click="goToNormalShop" type="success" style="margin-left: 20px;">普通商城</el-button>
            <!-- 我的订单按钮 -->
            <el-button @click="goToMyOrders" type="info" style="margin-left: 20px;">我的订单</el-button>
        </div>

        <!-- 商品展示区域 -->
        <div v-if="seckillProducts.length === 0">
            <p>抱歉，没有找到相关商品。</p>
        </div>
        <el-row :gutter="20">
            <el-col :span="6" v-for="product in seckillProducts" :key="product.id">
                <el-card :body-style="{ padding: '10px' }" class="product-card">
                    <img v-lazy="product.productImage" class="product-image" alt="图片加载失败"
                        @click="goToDetail(product.id)" />
                    <div class="product-info">
                        <h3 class="product-name">{{ product.productName }}</h3>
                        <p class="product-price">秒杀价: ￥{{ product.seckillPrice }}</p>
                        <p class="product-stock">库存：{{ product.stockCount }}</p>
                        <p class="product-time" :class="getStatusClass(product)">{{ getSeckillStatus(product) }}</p>
                        <!-- 倒计时显示 -->
                        <p v-if="countdowns[product.id]" class="product-countdown">
                            剩余时间：{{ countdowns[product.id] }}
                        </p>
                        <!-- 立即秒杀按钮 -->
                        <el-button type="warning" size="large" class="seckill-button" :disabled="!canSeckill(product)"
                            @click="openSeckill(product.goodsId, product.id)">
                            {{ canSeckill(product) ? "立即秒杀" : "秒杀未开始或已结束" }}
                        </el-button>
                        <!-- 验证码弹窗 -->
                        <el-dialog title="输入验证码" :visible.sync="captchaDialogVisible">
                            <div style="text-align: center;">
                                <img :src="captchaImage" alt="验证码" style="margin-bottom: 10px;" />
                                <el-button size="mini" @click="refreshCaptcha">刷新验证码</el-button>
                            </div>
                            <el-input v-model="captcha" placeholder="请输入验证码" style="margin-top: 10px;" />
                            <span slot="footer" class="dialog-footer">
                                <el-button @click="captchaDialogVisible = false">取消</el-button>
                                <el-button type="primary" @click="confirmSeckill">确认</el-button>
                            </span>
                        </el-dialog>
                    </div>
                </el-card>
            </el-col>
        </el-row>

        <!-- 分页控件 -->
        <div class="pagination">
            <el-pagination @current-change="handlePageChange" :current-page="currentPage" :page-size="pageSize"
                layout="prev, pager, next" :total="total">
            </el-pagination>
        </div>
    </div>
</template>

<script>
const API_BASE_URL = process.env.VUE_APP_API_BASE_URL;

export default {
    name: 'SeckillList',
    data() {
        return {
            searchKeyword: '', // 搜索关键字
            priceRange: {
                min: 0, // 最低价
                max: 99999999, // 最高价
            },
            seckillProducts: [], // 秒杀商品列表
            currentPage: 1, // 当前页码
            pageSize: 10, // 每页大小
            total: 0, // 总记录数
            countdowns: {}, // 用于存储商品的倒计时

            captchaDialogVisible: false, // 控制弹窗显示
            captchaImage: '', // 验证码图片
            captcha: '', // 用户输入的验证码
            currentProductId: null, // 当前商品ID
            currentSeckillProductId: null, // 当前秒杀商品ID
        };
    },
    mounted() {
        this.fetchSeckillProducts(); // 初始加载商品列表
        this.startCountdowns(); // 启动倒计时逻辑
    },
    methods: {
        async fetchSeckillProducts() {
            try {
                // 获取秒杀商品列表
                const response = await this.$axios.get(`${API_BASE_URL}/api/seckill/products`, {
                    params: {
                        page: this.currentPage,
                        size: this.pageSize,
                        keyword: this.searchKeyword,
                        priceMin: this.priceRange.min,
                        priceMax: this.priceRange.max,
                    },
                });
                if (response.code === 200) {
                    const { content, totalElements } = response.data;
                    this.seckillProducts = content;
                    this.total = totalElements;
                } else {
                    this.$message.error(response.message || '加载秒杀商品失败');
                }
            } catch (error) {
                console.error(error);
                // this.$message.error('加载秒杀商品失败');
            }
        },
        handleSearch() {
            this.currentPage = 1;
            this.fetchSeckillProducts();
        },
        handleFilter() {
            this.currentPage = 1;
            this.fetchSeckillProducts();
        },
        resetFilter() {
            this.searchKeyword = '';
            this.priceRange.min = null;
            this.priceRange.max = null;
            this.handleFilter();
        },
        handlePageChange(page) {
            this.currentPage = page;
            this.fetchSeckillProducts();
        },
        goToDetail(productId) {
            this.$router.push(`/seckill/product/${productId}`);
        },
        goToNormalShop() {
            this.$router.push('/products');
        },
        goToMyOrders() {
            this.$router.push('/orderlist');
        },
        formatDate(datetime) {
            if (!datetime) return '';
            const date = new Date(datetime);
            return date.toLocaleString();
        },
        canSeckill(product) {
            const now = new Date().getTime();
            const startTime = new Date(product.startDate).getTime();
            const endTime = new Date(product.endDate).getTime();
            return now >= startTime && now <= endTime && product.stockCount > 0;
        },
        // 打开秒杀弹窗
        async openSeckill(productId, seckillProductId) {
            this.currentProductId = productId;
            this.currentSeckillProductId = seckillProductId;
            await this.fetchCaptcha(); // 获取验证码图片
            this.captchaDialogVisible = true; // 打开弹窗
        },
        // 获取验证码图片
        async fetchCaptcha() {
            try {
                const response = await this.$axios.get(`${API_BASE_URL}/api/seckill/captcha`, {
                    responseType: 'blob', // 确保图片以二进制形式返回
                });
                this.captchaImage = URL.createObjectURL(response); // 将图片转换为 URL
            } catch (error) {
                this.$message.error('获取验证码失败');
                console.error(error);
            }
        },

        // 刷新验证码
        async refreshCaptcha() {
            await this.fetchCaptcha();
        },

        // 确认秒杀
        async confirmSeckill() {
            if (!this.captcha) {
                this.$message.warning('请输入验证码');
                return;
            }

            try {
                const response = await this.$axios.post(
                    `${API_BASE_URL}/api/seckill/start`,
                    {
                        captcha: this.captcha, // 验证码传递到后端
                    },
                    {
                        params: {
                            captcha: this.captcha, // 验证码传递到后端
                            productId: this.currentProductId,
                            seckillProductId: this.currentSeckillProductId,
                        },
                    }
                );

                if (response.code === 200) {
                    this.$message.success('秒杀成功！');
                    this.fetchSeckillProducts(); // 秒杀成功后刷新列表
                } else {
                    this.$message.error(response.message || '秒杀失败');
                }
            } catch (error) {
                console.error(error);
                this.$message.error('秒杀失败: ' + (error.response.data || error.message));
            } finally {
                this.captchaDialogVisible = false; // 关闭弹窗
                this.captcha = ''; // 清空验证码输入
            }
        },

        getSeckillStatus(product) {
            const now = new Date().getTime();
            const startTime = new Date(product.startDate).getTime();
            const endTime = new Date(product.endDate).getTime();

            if (now < startTime) {
                const timeDiff = startTime - now; // 距离开始的时间差（毫秒）

                // 转换为天、小时、分钟、秒
                const days = Math.floor(timeDiff / (1000 * 60 * 60 * 24));
                const hours = Math.floor((timeDiff % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
                const minutes = Math.floor((timeDiff % (1000 * 60 * 60)) / (1000 * 60));
                const seconds = Math.floor((timeDiff % (1000 * 60)) / 1000);

                // 返回格式化的时间
                return `距开始还有 ${days}天 ${hours}小时 ${minutes}分钟 ${seconds}秒`;
            } else if (now > endTime) {
                return "已结束";
            } else {
                return "正在进行中";
            }
        },
        getStatusClass(product) {
            const now = new Date().getTime();
            const startTime = new Date(product.startDate).getTime();
            const endTime = new Date(product.endDate).getTime();

            if (now < startTime) {
                return "product-countdown";
            } else if (now > endTime) {
                return "status-ended";
            } else {
                return "status-countdown";
            }
        },
        startCountdowns() {
            setInterval(() => {
                this.seckillProducts.forEach(product => {
                    if (!product || !product.id || !product.endDate || !product.startDate) return;

                    const now = new Date().getTime();
                    const endTime = new Date(product.endDate).getTime();
                    const startTime = new Date(product.startDate).getTime();

                    if (now >= startTime && now < endTime) {
                        const remaining = endTime - now;
                        this.$set(this.countdowns, product.id, this.formatTime(remaining));
                    } else {
                        this.$set(this.countdowns, product.id, null);
                    }
                });
            }, 1000);
        },
        formatTime(ms) {
            const seconds = Math.floor(ms / 1000) % 60;
            const minutes = Math.floor(ms / (1000 * 60)) % 60;
            const hours = Math.floor(ms / (1000 * 60 * 60)) % 24;
            const days = Math.floor(ms / (1000 * 60 * 60 * 24));
            return `${days > 0 ? `${days}天 ` : ''}${hours}小时 ${minutes}分 ${seconds}秒`;
        },
    },
};
</script>

<style scoped>
.seckill-list {
    padding: 20px;
}

.search-filter {
    margin-bottom: 20px;
    display: flex;
    align-items: center;
}

.product-card {
    cursor: pointer;
    margin-bottom: 20px;
    transition: box-shadow 0.3s ease;
}

.product-card:hover {
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.2);
}

.product-image {
    width: 100%;
    height: 200px;
    object-fit: cover;
    border-radius: 8px;
}

.product-info {
    text-align: center;
    margin-top: 10px;
}

.product-name {
    font-size: 16px;
    font-weight: bold;
    margin: 10px 0;
}

.product-price {
    color: red;
    font-size: 18px;
    margin-bottom: 5px;
}

.product-stock,
.product-time {
    font-size: 14px;
    color: #666;
}

.pagination {
    text-align: center;
    margin-top: 20px;
}

.seckill-button {
    width: 100%;
    font-size: 16px;
    font-weight: bold;
    margin-top: 10px;
}

.status-not-started {
    color: gray;
}

.status-ended {
    color: red;
}

.status-countdown {
    color: green;
}

.product-countdown {
    font-size: 14px;
    color: #ff9900;
    /* 橙色倒计时文本 */
    font-weight: bold;
    margin-top: 5px;
}
</style>
