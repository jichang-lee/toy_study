<template>
  <div>
    <ul>
      <li v-for="post in posts" :key="post.id">
        <div class="title">
          <router-link
            :to="{ name: 'read', params: { postId: post.id }, props: { postData: post } }"
          >
            {{ post.title }}
          </router-link>
        </div>
        
        <div class="content">
          {{ post.content }}
        </div>

      <div class="sub d-flex">
        <div class="category">개발</div>
        <div class="regDate">2023-11-04</div>
      </div>

      </li>
    </ul>
  </div>
</template>

<script setup lang="ts">
import axios from 'axios'
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const posts = ref([])
const router = useRouter()

axios
  .get('/api/posts', {
    params: {
      page: 1,
      size: 5
    }
  })
  .then((response) => {
    posts.value = response.data
  })

const moveToRead = (e) => {
  router.push({ name: 'read' })
}
</script>
<style scoped lang="scss">
ul {
  list-style: none;
  padding: 0;

  li {
    margin-bottom: 2rem;

    .title {
        
      a {
        font-size: 1.2rem;
        text-decoration: none;
        color: #383838;
      }

        &:hover {
            text-decoration: underline;
        }

      
    }
    .content {
        font-size: 0.85;
        margin-top: 8px;
        color: #5d5d5d;
      }
    &:last-child {
        margin-bottom: 0;
      }

    .sub {
        margin-top: 9px;
        font-size: 0.8rem;

        .regDate {
            margin-left : 10px;
            color: #6b6b6b;
        }
      }
  }
}
</style>