import React, { useState } from "react";
import logo from "./assets/logo.jpg";
import defaultprofile from "./assets/profileImage.png";
// import SideNav from "./Sidenav.tsx";
import "./Navigationbar.css";
import {Link} from 'react-router-dom';
import '../node_modules/bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/css/bootstrap.css';
import 'bootstrap/dist/js/bootstrap.js';
 
 
const Navigation: React.FC = () => {
  const [isOpen, setIsOpen] = useState<boolean>(false);
 
  const toggleNav = () => {
    setIsOpen(!isOpen);
  };
 
  return (
    <>
      <nav className="navbar navbar-dark bg-dark">
        <div className="container-fluid">
          <div className="logo-container">
            <button onClick={toggleNav} className="navbar-toggler" type="button">
              <span className="navbar-toggler-icon"></span>
            </button>
            <div>
              <img className="logo" src={logo} alt="" />
            </div>
          </div>
          {/* <div className="search-conatainer">
            <input type="text" name="" id="search" />
          </div> */}
          <div className="nav-item dropdown">
            <a
              className="nav-link dropdown-toggle"
              role="button"
              data-bs-toggle="dropdown"
            >
              <img className="user-image" src={defaultprofile} alt="noimage" />
            </a>
            <ul className="dropdown-menu">
              <li>
                <Link className="dropdown-item" to="/Login">
                  Login{" "}
                </Link>
              </li>
              <li>
                <hr className="dropdown-divider" />
              </li>
              <li>
                <Link className="dropdown-item" to="/Register">
                  Register
                </Link>
              </li>
            </ul>
          </div>
        </div>
      </nav>
      {/* <SideNav isOpen={isOpen} setIsOpen={setIsOpen} /> */}
    </>
  );
};
 
export default Navigation;