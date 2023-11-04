<template>
    <div>
        <el-input 
        v-model="posts.title"
        placeholder="제목을 입력해주세요"/>
    </div>

    <div class="mt-2">
        <el-input 
        v-model="posts.content"
        type="textarea" 
        rows="15"/>
    </div>

    <div class="mt-2 d-flext justify-content-end">
        <el-button 
        @click="edit"
        type="primary"> 글 수정</el-button>
    </div>
    

</template>

<script setup lang="ts">
import { ref,defineProps } from "vue";
import axios from 'axios';
import { useRouter } from "vue-router";

const props = defineProps({
    postId : {
        type : [Number,String],
        require : true,
    }
})

const title = ref("")
const content = ref("")
const router = useRouter()
const posts = ref([])

const edit = ()=>{
    axios.patch(`/api/posts/${props.postId}`,posts.value)
    .then(()=>{
        router.replace({ name : "home" })
    })
}


 axios.get(`/api/posts/${props.postId}`,{
}).then((response)=>{
    posts.value = response.data
})



</script>

<style>

</style>