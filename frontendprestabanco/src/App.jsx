import React, { useState, useEffect } from 'react';
import './App.css';
import Header from './components/Header';
import 'bootstrap/dist/css/bootstrap.min.css';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import Home from './components/Home';

function App() {
  const [count, setCount] = useState(0)

  return (
    <BrowserRouter>
      <Header/>
      <Routes>
        <Route path="/" element={<Home/>}/>
        <Route path="/clientView" element={<Home/>}/>
        <Route path="/ejecutiveView" element={<Home/>}/>
      </Routes>
    </BrowserRouter>
  )
}

export default App
