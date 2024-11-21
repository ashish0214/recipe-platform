// import React, { useState } from 'react';
// import { useNavigate } from 'react-router-dom';
// import {
//   Container,
//   Typography,
//   TextField,
//   Button,
//   Alert,
//   FormControl,
//   FormHelperText,
//   Input,
//   CircularProgress,
//   Grid,
// } from '@mui/material';
// import { useForm, Controller } from 'react-hook-form';
// import { useUser } from './UserContext';

// const Register: React.FC = () => {
//   const {
//     register,
//     handleSubmit,
//     control,
//     formState: { errors, isSubmitting },
//     watch,
//   } = useForm({
//     mode: 'onBlur',
//   });

//   const [errorMessage, setErrorMessage] = useState<string | null>(null);
//   const navigate = useNavigate();
//   const { setUser } = useUser();

//   // eslint-disable-next-line @typescript-eslint/no-explicit-any
//   const onSubmit = async (data: any) => {
//     try {
//       console.log(data.email, data);
//       //const response = await axios.post(`http://localhost:8080/authentication/validate/${data.email}`, data.email);
//       const response = await fetch(
//         `http://localhost:8080/authentication/validate`,
//         {
//           method: "POST",
//           headers: {
//             "Content-Type": "application/json",
//           },
//           body: JSON.stringify({ email: data.email }),
//         }
//       );
//       if (response.status === 200) {
//         setUser(data);
//         navigate("/OTPPage", {
//           state: {
//             email: data.email,
//             formData: data,
//             verificationType: "register",
//           },
//         });
//       } else{
//         console.log(response.status);
//         setErrorMessage(response);
//       }
//     } catch (error) {
//       if (error instanceof Error) {
//         setErrorMessage(error.message);
//       }else {
//         setErrorMessage("An unexpected error occurred.");
//       }
//     }
//   };

//   const validateEmail = (value: string) => {
//     // Basic email validation using regex
//     const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
//     if (!regex.test(value)) {
//       return 'Invalid email address format';
//     }

//     // Additional validation: no numbers after @
//     const parts = value.split('@');
//     if (parts.length === 2 && /\d/.test(parts[1])) {
//       return 'Email cannot have numbers immediately after @';
//     }
//     return true;
//   };

//   return (
//     <div style={{ minHeight: '100vh', display: 'flex', flexDirection: 'column', justifyContent: 'space-between', alignItems: 'center', padding: '16px' }}>
//       <Container
//         maxWidth="md"
//         style={{ maxWidth: '600px', width: '100%', background: 'white', padding: '24px', borderRadius: '8px', boxShadow: '0 0 10px rgba(0, 0, 0, 0.1)', marginTop: '40px', marginBottom: '16px' }}
//       >
//         <Typography variant="h4" component="h1" align="center" gutterBottom>
//           Registration Form
//         </Typography>
//         <form onSubmit={handleSubmit(onSubmit)}>
//           {errorMessage && (
//             <Alert severity="error" style={{ marginBottom: '10px' }}>
//               {errorMessage}
//             </Alert>
//           )}
//           <Grid container spacing={2}>
//             <Grid item xs={12} sm={6}>
//               <TextField
//                 {...register('firstName', {
//                   required: 'First name is required',
//                   maxLength: { value: 30, message: 'Max 30 characters' },
//                   pattern: {
//                     value: /^[a-zA-Z]+$/,
//                     message: 'First name should contain only alphabets',
//                   },
//                 })}
//                 label="First Name"
//                 error={Boolean(errors.firstName)}
//                 helperText={errors.firstName?.message as React.ReactNode}
//                 fullWidth
//               />
//             </Grid>
//             <Grid item xs={12} sm={6}>
//               <TextField
//                 {...register('lastName', {
//                   required: 'Last name is required',
//                   maxLength: { value: 30, message: 'Max 30 characters' },
//                   pattern: {
//                     value: /^[a-zA-Z]+$/,
//                     message: 'Last name should contain only alphabets',
//                   },
//                 })}
//                 label="Last Name"
//                 error={Boolean(errors.lastName)}
//                 helperText={errors.lastName?.message as React.ReactNode}
//                 fullWidth
//               />
//             </Grid>
//             <Grid item xs={12}>
//               <TextField
//                 {...register('email', {
//                   required: 'Email is required',
//                   maxLength: { value: 30, message: 'Max 30 characters' },
//                   validate: validateEmail,
//                 })}
//                 type="email"
//                 label="Email"
//                 error={Boolean(errors.email)}
//                 helperText={errors.email?.message as React.ReactNode}
//                 fullWidth
//               />
//             </Grid>
//             <Grid item xs={12} sm={6}>
//               <TextField
//                 {...register('password', {
//                   required: 'Password is required',
//                   maxLength: { value: 30, message: 'Max 30 characters' },
//                   pattern: {
//                     value: /^(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*])(?=.*[a-z]).{8,30}$/,
//                     message: 'Password should be at least 8 characters long, contain a digit, an uppercase letter, a lowercase letter, and a special character',
//                   },
//                 })}
//                 type="password"
//                 label="Password"
//                 error={Boolean(errors.password)}
//                 helperText={errors.password?.message as React.ReactNode}
//                 fullWidth
//               />
//             </Grid>
//             <Grid item xs={12} sm={6}>
//               <TextField
//                 {...register('confirmPassword', {
//                   required: 'Confirm password is required',
//                   maxLength: { value: 30, message: 'Max 30 characters' },
//                   validate: value => value === watch('password') || 'Passwords do not match',
//                 })}
//                 type="password"
//                 label="Confirm Password"
//                 error={Boolean(errors.confirmPassword)}
//                 helperText={errors.confirmPassword?.message as React.ReactNode}
//                 fullWidth
//               />
//             </Grid>
//             <Grid item xs={12}>
//               <FormControl fullWidth error={Boolean(errors.image)}>
//                 <Controller
//                   name="image"
//                   control={control}
//                   defaultValue={[]}
//                   rules={{
//                     validate: {
//                       fileSize: files => {
//                         if (files.length === 0) return true;
//                         return (files[0].size / 1024 / 1024) <= 10 || 'File size should be less than 10MB';
//                       },
//                       fileType: files => {
//                         if (files.length === 0) return true;
//                         const acceptedFileTypes = ['image/jpeg', 'image/png', 'image/gif'];
//                         return acceptedFileTypes.includes(files[0].type) || 'Invalid file type. Only jpeg, png and gif types are accepted';
//                       },
//                     },
//                   }}
//                   render={({ field }) => (
//                     <Input
//                       type="file"
//                       inputProps={{ accept: 'image/*' }}
//                       onChange={e => {
//                         const files = (e.target as HTMLInputElement).files;
//                         field.onChange(files ? files : []);
//                       }}
//                     />
//                   )}
//                 />
//                 {errors.image && (
//                   <FormHelperText>{errors.image.message as React.ReactNode}</FormHelperText>
//                 )}
//               </FormControl>
//             </Grid>
//             <Grid item xs={12}>
//               <Button
//                 type="submit"
//                 variant="contained"
//                 color="success"
//                 disabled={isSubmitting}
//                 fullWidth
//                 style={{ marginTop: '20px' }}
//               >
//                 {isSubmitting ? <CircularProgress size={24} /> : 'Register'}
//               </Button>
//             </Grid>
//           </Grid>
//         </form>
//       </Container>
//       <footer style={{ width: '100%', background: '#3f51b5', color: 'white', padding: '16px', textAlign: 'center' }}>
//         <Typography variant="body2">
//           &copy; 2024 Your Company. All rights reserved.
//         </Typography>
//       </footer>
//     </div>
//   );
// };

// export default Register;

import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import {
  Container,
  Typography,
  TextField,
  Button,
  Alert,
  FormControl,
  FormHelperText,
  Input,
  CircularProgress,
  Grid,
  IconButton,
  InputAdornment,
} from "@mui/material";
import { Visibility, VisibilityOff } from "@mui/icons-material";
import { useForm, Controller } from "react-hook-form";
import { useUser } from "./UserContext";

const Register: React.FC = () => {
  const {
    register,
    handleSubmit,
    control,
    formState: { errors, isSubmitting },
    watch,
  } = useForm({
    mode: "onBlur",
  });

  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const [successMessage, setSuccessMessage] = useState<string | null>(null);
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);
  const navigate = useNavigate();
  const { setUser } = useUser();

  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  const onSubmit = async (data: any) => {
    try {
      console.log(data.email, data);
      //const response = await axios.post(`http://localhost:8080/authentication/validate/${data.email}`, data.email);
      const response = await fetch(
        `http://localhost:8080/authentication/validate`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({ email: data.email }),
        }
      );
      if (response.status === 200) {
        setUser(data);
        const successResponse = await response.text();
        setSuccessMessage(successResponse);
        console.log(successResponse);
        setErrorMessage(null);
        navigate("/OTPPage", {
          state: {
            email: data.email,
            formData: data,
            verificationType: "register",
          },
        });
      } else {
        const errorResponse = await response.text();
        setErrorMessage(errorResponse || "Registration failed.");
        setSuccessMessage(null);
      }
    } catch (error) {
      if (error instanceof Error) {
        setErrorMessage(error.message);
      } else {
        setErrorMessage("An unexpected error occurred.");
      }
      setSuccessMessage(null);
    }
  };

  const validateEmail = (value: string) => {
    // Basic email validation using regex
    const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!regex.test(value)) {
      return "Invalid email address format";
    }

    // Additional validation: only alphabets after  @
 
  const parts = value.split("@");
  if (parts.length === 2 && !/^[a-zA-Z]+\.[^\s@]+$/.test(parts[1])) {
    return "Email can only contain alphabets after @";
  }
  return true;
  }

  const handleClickShowPassword = () => setShowPassword((show) => !show);
  const handleClickShowConfirmPassword = () =>
    setShowConfirmPassword((show) => !show);

  return (
    <div
      style={{
        minHeight: "100vh",
        display: "flex",
        flexDirection: "column",
        justifyContent: "space-between",
        alignItems: "center",
        padding: "16px",
      }}
    >
      <Container
        maxWidth="md"
        style={{
          maxWidth: "600px",
          width: "100%",
          background: "white",
          padding: "24px",
          borderRadius: "8px",
          boxShadow: "0 0 10px rgba(0, 0, 0, 0.1)",
          marginTop: "40px",
          marginBottom: "16px",
        }}
      >
        <Typography variant="h4" component="h1" align="center" gutterBottom>
          Registration Form
        </Typography>
        <form onSubmit={handleSubmit(onSubmit)}>
          {errorMessage && (
            <Alert severity="error" style={{ marginBottom: "10px" }}>
              {errorMessage}
            </Alert>
          )}
          {successMessage && (
            <Alert severity="success" style={{ marginBottom: "10px" }}>
              {successMessage}
            </Alert>
          )}
          <Grid container spacing={2}>
            <Grid item xs={12} sm={6}>
              <TextField
                {...register("firstName", {
                  required: "First name is required",
                  maxLength: { value: 30, message: "Max 30 characters" },
                  pattern: {
                    value: /^[a-zA-Z]+$/,
                    message: "First name should contain only alphabets",
                  },
                })}
                label="First Name"
                error={Boolean(errors.firstName)}
                helperText={errors.firstName?.message as React.ReactNode}
                fullWidth
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <TextField
                {...register("lastName", {
                  required: "Last name is required",
                  maxLength: { value: 30, message: "Max 30 characters" },
                  pattern: {
                    value: /^[a-zA-Z]+$/,
                    message: "Last name should contain only alphabets",
                  },
                })}
                label="Last Name"
                error={Boolean(errors.lastName)}
                helperText={errors.lastName?.message as React.ReactNode}
                fullWidth
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                {...register("email", {
                  required: "Email is required",
                  maxLength: { value: 30, message: "Max 30 characters" },
                  validate: validateEmail,
                })}
                type="email"
                label="Email"
                error={Boolean(errors.email)}
                helperText={errors.email?.message as React.ReactNode}
                fullWidth
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <TextField
                {...register("password", {
                  required: "Password is required",
                  maxLength: { value: 30, message: "Max 30 characters" },
                  pattern: {
                    value:
                      /^(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*])(?=.*[a-z]).{8,30}$/,
                    message:
                      "Password should be at least 8 characters long, contain a digit, an uppercase letter, a lowercase letter, and a special character",
                  },
                })}
                type={showPassword ? "text" : "password"}
                label="Password"
                error={Boolean(errors.password)}
                helperText={errors.password?.message as React.ReactNode}
                fullWidth
                InputProps={{
                  endAdornment: (
                    <InputAdornment position="end">
                      <IconButton
                        aria-label="toggle password visibility"
                        onClick={handleClickShowPassword}
                        edge="end"
                      >
                        {showPassword ? <VisibilityOff /> : <Visibility />}
                      </IconButton>
                    </InputAdornment>
                  ),
                }}
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <TextField
                {...register("confirmPassword", {
                  required: "Confirm password is required",
                  maxLength: { value: 30, message: "Max 30 characters" },
                  validate: (value) =>
                    value === watch("password") || "Passwords do not match",
                })}
                type={showConfirmPassword ? "text" : "password"}
                label="Confirm Password"
                error={Boolean(errors.confirmPassword)}
                helperText={errors.confirmPassword?.message as React.ReactNode}
                fullWidth
                InputProps={{
                  endAdornment: (
                    <InputAdornment position="end">
                      <IconButton
                        aria-label="toggle confirm password visibility"
                        onClick={handleClickShowConfirmPassword}
                        edge="end"
                      >
                        {showConfirmPassword ? (
                          <VisibilityOff />
                        ) : (
                          <Visibility />
                        )}
                      </IconButton>
                    </InputAdornment>
                  ),
                }}
              />
            </Grid>
            <Grid item xs={12}>
              <FormControl fullWidth error={Boolean(errors.image)}>
                <Controller
                  name="image"
                  control={control}
                  defaultValue={[]}
                  rules={{
                    validate: {
                      fileSize: (files) => {
                        if (files.length === 0) return true;
                        return (
                          files[0].size / 1024 / 1024 <= 10 ||
                          "File size should be less than 10MB"
                        );
                      },
                      fileType: (files) => {
                        if (files.length === 0) return true;
                        const acceptedFileTypes = [
                          "image/jpeg",
                          "image/png",
                          "image/gif",
                        ];
                        return (
                          acceptedFileTypes.includes(files[0].type) ||
                          "Invalid file type. Only jpeg, png and gif types are accepted"
                        );
                      },
                    },
                  }}
                  render={({ field }) => (
                    <Input
                      type="file"
                      inputProps={{ accept: "image/*" }}
                      onChange={(e) => {
                        const files = (e.target as HTMLInputElement).files;
                        field.onChange(files ? files : []);
                      }}
                    />
                  )}
                />
                {errors.image && (
                  <FormHelperText>
                    {errors.image.message as React.ReactNode}
                  </FormHelperText>
                )}
              </FormControl>
            </Grid>
            <Grid item xs={12}>
              <Button
                type="submit"
                variant="contained"
                color="success"
                disabled={isSubmitting}
                fullWidth
                style={{ marginTop: "20px" }}
              >
                {isSubmitting ? <CircularProgress size={24} /> : "Register"}
              </Button>
            </Grid>
          </Grid>
        </form>
      </Container>
      <footer
        style={{
          width: "100%",
          background: "#3f51b5",
          color: "white",
          padding: "16px",
          textAlign: "center",
        }}
      >
        <Typography variant="body2">
          &copy; 2024 Your Company. All rights reserved.
        </Typography>
      </footer>
    </div>
  );
};

export default Register;
