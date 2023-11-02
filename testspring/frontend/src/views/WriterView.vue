<template>
    <div>
        <input 
        v-model="title"
        placeholder="제목을 입력해주세요"/>
    </div>

    <div>
        <textarea 
        v-model="content"
         
        rows="15"/>
    </div>

    <div >
        <button 
        @click="write"
        > 글 작성완료</button>
    </div>
    

</template>

<script setup lang="ts">
import { ref } from "vue";
import axios from 'axios';


const title = ref("")
const content = ref("")



const write = ()=>{
    axios.post("http://localhost:8099/posts",{
     title: title.value,
     content : content.value   
    })
}
</script>

<style>

</style>