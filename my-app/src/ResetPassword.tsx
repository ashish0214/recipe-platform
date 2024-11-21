// import React, { useState } from 'react';
// import { TextField, Button, IconButton, InputAdornment, Container, Typography, Card, CardContent } from '@mui/material';
// import { Visibility, VisibilityOff } from '@mui/icons-material';
// import axios from 'axios';
// import { useNavigate, useLocation } from 'react-router-dom';

// interface ResetPasswordProps {
//   email: string;
// }

// const ResetPassword: React.FC = () => {
//   const location = useLocation();
//   const { email } = location.state as ResetPasswordProps;

//   const [newPassword, setNewPassword] = useState('');
//   const [confirmPassword, setConfirmPassword] = useState('');
//   const [showNewPassword, setShowNewPassword] = useState(false);
//   const [showConfirmPassword, setShowConfirmPassword] = useState(false);
//   const [errors, setErrors] = useState<{ newPassword?: string; confirmPassword?: string }>({});
//   const navigate = useNavigate();

//   const validatePassword = (password: string): string | undefined => {
//     const passwordRegex = /^(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
//     return passwordRegex.test(password) ? undefined : 'Password must be at least 8 characters long, contain an uppercase letter, a number, and a special character.';
//   };

//   const handleNewPasswordChange = (event: React.ChangeEvent<HTMLInputElement>) => {
//     const { value } = event.target;
//     setNewPassword(value);
//     setErrors((prevErrors) => ({ ...prevErrors, newPassword: validatePassword(value) }));
//   };

//   const handleConfirmPasswordChange = (event: React.ChangeEvent<HTMLInputElement>) => {
//     const { value } = event.target;
//     setConfirmPassword(value);
//     setErrors((prevErrors) => ({
//       ...prevErrors,
//       confirmPassword: value === newPassword ? undefined : 'Passwords do not match.',
//     }));
//   };

//   const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
//     event.preventDefault();
//     if (!errors.newPassword && !errors.confirmPassword && newPassword && confirmPassword) {
//       try {
//         await axios.patch(`http://localhost:8080/users/changePassword/${email}`, { email, newPassword });
//         navigate('/login');
//       } catch (error) {
//         console.error('Error resetting password:', error);
//       }
//     }
//   };

//   return (
//     <Container maxWidth="sm" style={{ marginTop: '20px' }}>
//       <Card style={{ boxShadow: '0px 3px 6px rgba(0, 0, 0, 0.16)' }}>
//         <CardContent>
//           <Typography variant="h4" gutterBottom>
//             Reset Password
//           </Typography>
//           <form onSubmit={handleSubmit}>
//             <TextField
//               label="Email"
//               value={email}
//               fullWidth
//               margin="normal"
//               disabled
//             />
//             <TextField
//               label="New Password"
//               type={showNewPassword ? 'text' : 'password'}
//               value={newPassword}
//               onChange={handleNewPasswordChange}
//               fullWidth
//               margin="normal"
//               error={!!errors.newPassword}
//               helperText={errors.newPassword}
//               InputProps={{
//                 endAdornment: (
//                   <InputAdornment position="end">
//                     <IconButton
//                       onClick={() => setShowNewPassword((show) => !show)}
//                     >
//                       {showNewPassword ? <VisibilityOff /> : <Visibility />}
//                     </IconButton>
//                   </InputAdornment>
//                 ),
//               }}
//             />
//             <TextField
//               label="Confirm Password"
//               type={showConfirmPassword ? 'text' : 'password'}
//               value={confirmPassword}
//               onChange={handleConfirmPasswordChange}
//               fullWidth
//               margin="normal"
//               error={!!errors.confirmPassword}
//               helperText={errors.confirmPassword}
//               InputProps={{
//                 endAdornment: (
//                   <InputAdornment position="end">
//                     <IconButton
//                       onClick={() => setShowConfirmPassword((show) => !show)}
//                     >
//                       {showConfirmPassword ? <VisibilityOff /> : <Visibility />}
//                     </IconButton>
//                   </InputAdornment>
//                 ),
//               }}
//             />
//             <Button
//               type="submit"
//               variant="contained"
//               color="primary"
//               fullWidth
//               disabled={!!errors.newPassword || !!errors.confirmPassword || !newPassword || !confirmPassword}
//               style={{ marginTop: '16px' }}
//             >
//               Change Password
//             </Button>
//           </form>
//         </CardContent>
//       </Card>
//     </Container>
//   );
// };

// export default ResetPassword;

import React, { useState } from "react";
import {
  TextField,
  Button,
  IconButton,
  InputAdornment,
  Container,
  Typography,
  Card,
  CardContent,
  Alert,
} from "@mui/material";
import { Visibility, VisibilityOff } from "@mui/icons-material";
import { useNavigate, useLocation } from "react-router-dom";

interface ResetPasswordProps {
  email: string;
}

const ResetPassword: React.FC = () => {
  const location = useLocation();
  const { email } = location.state as ResetPasswordProps;

  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [showNewPassword, setShowNewPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);
  const [errors, setErrors] = useState<{
    newPassword?: string;
    confirmPassword?: string;
  }>({});
  const [successMessage, setSuccessMessage] = useState<string | null>(null);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const navigate = useNavigate();

  const validatePassword = (password: string): string | undefined => {
    const passwordRegex =
      /^(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
    return passwordRegex.test(password)
      ? undefined
      : "Password must be at least 8 characters long, contain an uppercase letter, a number, and a special character.";
  };

  const handleNewPasswordChange = (
    event: React.ChangeEvent<HTMLInputElement>
  ) => {
    const { value } = event.target;
    setNewPassword(value);
    setErrors((prevErrors) => ({
      ...prevErrors,
      newPassword: validatePassword(value),
    }));
  };

  const handleConfirmPasswordChange = (
    event: React.ChangeEvent<HTMLInputElement>
  ) => {
    const { value } = event.target;
    setConfirmPassword(value);
    setErrors((prevErrors) => ({
      ...prevErrors,
      confirmPassword:
        value === newPassword ? undefined : "Passwords do not match.",
    }));
  };

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    if (
      !errors.newPassword &&
      !errors.confirmPassword &&
      newPassword &&
      confirmPassword
    ) {
      try {
        const response = await fetch(
          `http://localhost:8080/users/changePassword/${email}`,
          {
            method: "PATCH",
            headers: {
              "Content-Type": "application/json",
            },
            body: JSON.stringify({ email, newPassword }),
          }
        );
        const data = await response.text();
        if (response.ok) {
          setSuccessMessage(data || "Password changed successfully.");
          setTimeout(() => navigate("/login"), 2000);
        } else {
          setErrorMessage(
            data || "Failed to reset password. Please try again."
          );
        }
      } catch (error) {
        setErrorMessage("An unexpected error occurred. Please try again.");
      }
    }
  };

  return (
    <Container maxWidth="sm" style={{ marginTop: "20px" }}>
      <Card style={{ boxShadow: "0px 3px 6px rgba(0, 0, 0, 0.16)" }}>
        <CardContent>
          <Typography variant="h4" gutterBottom>
            Reset Password
          </Typography>
          {successMessage && (
            <Alert severity="success" style={{ marginBottom: "10px" }}>
              {successMessage}
            </Alert>
          )}
          {errorMessage && (
            <Alert severity="error" style={{ marginBottom: "10px" }}>
              {errorMessage}
            </Alert>
          )}
          <form onSubmit={handleSubmit}>
            <TextField
              label="Email"
              value={email}
              fullWidth
              margin="normal"
              disabled
            />
            <TextField
              label="New Password"
              type={showNewPassword ? "text" : "password"}
              value={newPassword}
              onChange={handleNewPasswordChange}
              fullWidth
              margin="normal"
              error={!!errors.newPassword}
              helperText={errors.newPassword}
              InputProps={{
                endAdornment: (
                  <InputAdornment position="end">
                    <IconButton
                      onClick={() => setShowNewPassword((show) => !show)}
                    >
                      {showNewPassword ? <VisibilityOff /> : <Visibility />}
                    </IconButton>
                  </InputAdornment>
                ),
              }}
            />
            <TextField
              label="Confirm Password"
              type={showConfirmPassword ? "text" : "password"}
              value={confirmPassword}
              onChange={handleConfirmPasswordChange}
              fullWidth
              margin="normal"
              error={!!errors.confirmPassword}
              helperText={errors.confirmPassword}
              InputProps={{
                endAdornment: (
                  <InputAdornment position="end">
                    <IconButton
                      onClick={() => setShowConfirmPassword((show) => !show)}
                    >
                      {showConfirmPassword ? <VisibilityOff /> : <Visibility />}
                    </IconButton>
                  </InputAdornment>
                ),
              }}
            />
            <Button
              type="submit"
              variant="contained"
              color="primary"
              fullWidth
              disabled={
                !!errors.newPassword ||
                !!errors.confirmPassword ||
                !newPassword ||
                !confirmPassword
              }
              style={{ marginTop: "16px" }}
            >
              Change Password
            </Button>
          </form>
        </CardContent>
      </Card>
    </Container>
  );
};

export default ResetPassword;
