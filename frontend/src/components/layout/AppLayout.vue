<script setup lang="ts">
import NavBar from './NavBar.vue'
import AppFooter from './AppFooter.vue'
import { RouterView } from 'vue-router'
</script>

<template>
  <div class="cosmic-bg min-h-screen flex flex-col">
    <NavBar />
    <main class="flex-1 w-full max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <RouterView v-slot="{ Component, route }">
        <Transition name="slide" mode="out-in">
          <component :is="Component" :key="route.fullPath" />
        </Transition>
      </RouterView>
    </main>
    <AppFooter />
  </div>
</template>
