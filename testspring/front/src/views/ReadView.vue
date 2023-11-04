<template>
  <el-row>
    <el-col>
      <h2 class="title">{{ posts.title }}</h2>

      <div class="sub d-flex">
        <div class="category">개발</div>
        <div class="regDate">2023-11-04</div>
      </div>

    </el-col>
  </el-row>

  <el-row class="mt-3">
    <el-col>
      <div class="content">{{ posts.content }}</div>
    </el-col>
  </el-row>

  <el-row class="mt-3">
    <el-col>
      <div class="d-flex justify-content-end">
        <el-button type="warning" @click="moveToEdit">수정</el-button>
      </div>
    </el-col>
  </el-row>
</template>

<script setup lang="ts">
import axios from 'axios'
import { defineProps, ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const props = defineProps({
  postId: {
    type: [Number, String],
    require: true
  }
})

const posts = ref([])
const router = useRouter()

const moveToEdit = () => {
  router.replace({ name: 'edit', params: { postId: props.postId } })
}

onMounted(() => {
  axios.get(`/api/posts/${props.postId}`, {}).then((response) => {
    posts.value = response.data
  })
})
</script>

<style scoped lang="scss">
.title {
  font-size: 1.6rem;
  font-weight: 600;
  color: #383838;
  margin: 0;
}
.content {
  font-size: 0.95;
  margin-top: 8px;
  color: #616161;
  white-space: break-spaces;
  line-height: 1.5;
}

.sub {
  margin-top: 10px;
  font-size: 0.78rem;

  .regDate {
    margin-left: 10px;
    color: #6b6b6b;
  }
}
</style>