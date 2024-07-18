<script setup lang="ts">
 import { ref, onMounted, watch } from 'vue'
    import axios from 'axios'


defineProps<{
  msg: string
}>()

   class CatGifs {
        url: string
    }

const catGif = ref("")
const fetchingGifs = ref(false)

    async function fetchGif() {
        const catGifsResponse = await "https://cataas.com/cat/gif"
        catGif.value = catGifsResponse
    }
    onMounted(async () => {
        await fetchGif()
    })

    watch(catGif,(newGif)=>{
    console.log(newGif)})
</script>

<template>
  <div class="greetings">
    <h1 class="green">{{ msg }}</h1>

   <div class="row mt-3">
    <img :src="catGif">
               <div class="col-md-12 text-center">
                   <button @click="fetchGif" class="btn btn-md btn-primary">{{ fetchingGifs ? '...' : 'Load more' }}</button>
               </div>
           </div>
  </div>
</template>

<style scoped>
h1 {
  font-weight: 500;
  font-size: 2.6rem;
  position: relative;
  top: -10px;
}

h3 {
  font-size: 1.2rem;
}

.greetings h1,
.greetings h3 {
  text-align: center;
}

@media (min-width: 1024px) {
  .greetings h1,
  .greetings h3 {
    text-align: left;
  }
}
</style>
