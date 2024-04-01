<template>
  <i-frame v-model:src="url"></i-frame>
</template>

<script setup>
import iFrame from '@/components/iFrame'
import {ref} from "vue";

const url = ref("http://127.0.0.1:7012/doc.html")
</script>