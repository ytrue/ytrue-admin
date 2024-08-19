<template>
   <i-frame v-model:src="url"></i-frame>
</template>

<script setup>
import iFrame from '@/components/iFrame'

//const url = ref(import.meta.env.VITE_APP_BASE_API + "/doc.html")
const url = ref("http://127.0.0.1:7001/wallboard")
</script>
