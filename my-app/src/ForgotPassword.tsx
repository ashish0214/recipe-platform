// import React, { useState } from 'react';
// import { TextField, Button, Container, Typography, Alert, Card, CardContent } from '@mui/material';
// import axios from 'axios';
// import { useNavigate } from 'react-router-dom';

// const ForgotPasswordForm: React.FC = () => {
//   const [email, setEmail] = useState('');
//   const [emailError, setEmailError] = useState('');
//   const [snackbarOpen, setSnackbarOpen] = useState(false);
//   const [snackbarMessage, setSnackbarMessage] = useState('');
//   const [snackbarSeverity, setSnackbarSeverity] = useState<'success' | 'error'>('success');

//   const navigate = useNavigate();

//   const validateEmail = (email: string) => {
//     const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
//     const hasNoSpace = !/\s/.test(email);
//     const noNumberAfterAt = !/@[0-9]/.test(email);
    
//     if (!emailRegex.test(email)) {
//       return 'Invalid email format';
//     } else if (!hasNoSpace) {
//       return 'Email should not contain spaces';
//     } else if (!noNumberAfterAt) {
//       return 'Email should not have a number immediately after "@"';
//     }
//     return '';
//   };

//   const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
//     const { value } = e.target;
//     setEmail(value);
//     setEmailError(validateEmail(value));
//   };

//   const handleSubmit = async (e: React.FormEvent) => {
//     e.preventDefault();
//     if (!emailError) {
//       try {
//        const response = await axios.post('http://localhost:8080/authentication/forgot-email', { email });
//         console.log(response);
//         if(response.status === 200){
//            setSnackbarMessage(response.data);
//            setSnackbarSeverity("success");
//            setSnackbarOpen(true);
//              setTimeout(() => {
//                navigate("/OTPPage", {
//                  state: {
//                    email: email,
//                    verificationType: "forgotPassword",
//                  },
//                });
//              }, 2000);
//         }
//         // setSnackbarMessage('Verification email sent successfully!');
//         // setSnackbarSeverity('success');
//         // setSnackbarOpen(true);
      
//       // eslint-disable-next-line @typescript-eslint/no-explicit-any
//       } catch (error:any) {
//         if (error.response) {
//           if (error.response.status === 404) {
//             setSnackbarMessage('Email not found. Please enter a valid email address.');
//             setSnackbarSeverity('error');
//             setSnackbarOpen(true);
//           } else if (error.response.status === 409) {
//             setSnackbarMessage(
//               "OTP already sent to this email. Please check your email and enter the OTP."
//             );
//             setSnackbarSeverity("error");
//             setSnackbarOpen(true);
//           }
//           else{
//             setSnackbarMessage(
//               "Failed to send verification email. Please try again."
//             );
//             setSnackbarSeverity("error");
//             setSnackbarOpen(true);
//           }
//         } else {
//           // Handle other types of errors (e.g., network errors)
//           setSnackbarMessage('An unexpected error occurred. Please try again.');
//           setSnackbarSeverity('error');
//           setSnackbarOpen(true);
//         }
//       }
//     }
//   };

//   const handleCloseSnackbar = () => {
//     setSnackbarOpen(false);
//   };

//   return (
//     <Container
//       maxWidth="lg"
//       style={{
//         display: "flex",
//         justifyContent: "center",
//         alignItems: "center",
//         height: "90vh",
//       }}
//     >
//       <Card>
//         <CardContent>
//           <Typography variant="h4" gutterBottom>
//             Forgot Password
//           </Typography>
//           {snackbarOpen && (
//             <Alert
//               onClose={handleCloseSnackbar}
//               severity={snackbarSeverity}
//               style={{ marginBottom: "16px" }}
//             >
//               {snackbarMessage}
//             </Alert>
//           )}
//           <form onSubmit={handleSubmit}>
//             <TextField
//               label="Email"
//               variant="outlined"
//               fullWidth
//               margin="normal"
//               value={email}
//               onChange={handleChange}
//               error={Boolean(emailError)}
//               helperText={emailError}
//             />
//             <Button
//               type="submit"
//               variant="contained"
//               color="primary"
//               disabled={!email || Boolean(emailError)}
//               fullWidth
//             >
//               Send OTP
//             </Button>
//           </form>
          
//         </CardContent>
//       </Card>
//     </Container>
//   );
// };

// export default ForgotPasswordForm;

import React, { useState } from "react";
import {
  TextField,
  Button,
  Container,
  Typography,
  Alert,
  Card,
  CardContent,
} from "@mui/material";
import { useNavigate } from "react-router-dom";

const ForgotPasswordForm: React.FC = () => {
  const [email, setEmail] = useState("");
  const [emailError, setEmailError] = useState("");
  const [snackbarOpen, setSnackbarOpen] = useState(false);
  const [snackbarMessage, setSnackbarMessage] = useState("");
  const [snackbarSeverity, setSnackbarSeverity] = useState<"success" | "error">(
    "success"
  );

  const navigate = useNavigate();

  const validateEmail = (email: string) => {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    const hasNoSpace = !/\s/.test(email);
    const noNumberAfterAt = !/@[0-9]/.test(email);

    if (!emailRegex.test(email)) {
      return "Invalid email format";
    } else if (!hasNoSpace) {
      return "Email should not contain spaces";
    } else if (!noNumberAfterAt) {
      return 'Email should not have a number immediately after "@"';
    }
    return "";
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { value } = e.target;
    setEmail(value);
    setEmailError(validateEmail(value));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!emailError) {
      try {
        const response = await fetch(
          "http://localhost:8080/authentication/forgot-password",
          {
            method: "POST",
            headers: {
              "Content-Type": "application/json",
            },
            body: JSON.stringify({ email }),
          }
        );

        const data = await response.text();

        if (response.status === 200) {
          setSnackbarMessage(
            data || "Verification email sent successfully!"
          );
          setSnackbarSeverity("success");
          setSnackbarOpen(true);
          setTimeout(() => {
            navigate("/OTPPage", {
              state: {
                email: email,
                verificationType: "forgotPassword",
              },
            });
          }, 2000);
          console.log(data, response.status)
        } else if (response.status === 404) {
          console.log(response.status);
          setSnackbarMessage( "Email not found. Please enter a valid email address.");
          setSnackbarSeverity("error");
          setSnackbarOpen(true);
        } else if (response.status === 409) {
          setSnackbarMessage(
            "OTP already sent to this email. Please check your email and enter the OTP."
          );
          setSnackbarSeverity("error");
          setSnackbarOpen(true);
        } else {
          setSnackbarMessage(
            "Failed to send verification email. Please try again."
          );
          setSnackbarSeverity("error");
          setSnackbarOpen(true);
        }
      } catch (error) {
        setSnackbarMessage("An unexpected error occurred. Please try again.");
        setSnackbarSeverity("error");
        setSnackbarOpen(true);
      }
    }
  };

  const handleCloseSnackbar = () => {
    setSnackbarOpen(false);
  };

  return (
    <Container
      maxWidth="lg"
      style={{
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        height: "90vh",
      }}
    >
      <Card>
        <CardContent>
          <Typography variant="h4" gutterBottom>
            Forgot Password
          </Typography>
          {snackbarOpen && (
            <Alert
              onClose={handleCloseSnackbar}
              severity={snackbarSeverity}
              style={{ marginBottom: "16px" }}
            >
              {snackbarMessage}
            </Alert>
          )}
          <form onSubmit={handleSubmit}>
            <TextField
              label="Email"
              variant="outlined"
              fullWidth
              margin="normal"
              value={email}
              onChange={handleChange}
              error={Boolean(emailError)}
              helperText={emailError}
            />
            <Button
              type="submit"
              variant="contained"
              color="primary"
              disabled={!email || Boolean(emailError)}
              fullWidth
            >
              Send OTP
            </Button>
          </form>
        </CardContent>
      </Card>
    </Container>
  );
};

export default ForgotPasswordForm;

