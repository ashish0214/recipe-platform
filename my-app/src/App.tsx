import React from 'react';
import { BrowserRouter as Router, Route, Routes  } from 'react-router-dom';
import Login from './Login';
import Register from './Register';
import Navigation from './Navigationbar';
import OTPPage from './OTPPage';
import ForgotPassword from './ForgotPassword';
import ResetPassword from './ResetPassword';

const App: React.FC = () => {
  return (
    <>
    <Router>
    <Navigation/>
    <Routes>
        <Route path="/login"  element={<Login/>} ></Route>
        <Route path="/register"  element={<Register/>} ></Route>
        <Route path="/OTPPage"  element={<OTPPage/>} ></Route>
        <Route path="/ForgotPassword" element={<ForgotPassword/>} ></Route>
        <Route path="/ResetPassword" element={<ResetPassword/>} ></Route>
        </Routes>
    </Router>
    </>
  );
};

export default App;