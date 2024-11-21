// import React, { useState } from "react";
// import { Link as RouterLink, useNavigate } from "react-router-dom";
// import {
//   Container,
//   Typography,
//   TextField,
//   Button,
//   Link,
//   CircularProgress,
//   Grid,
// } from "@mui/material";
// import { useForm, SubmitHandler } from "react-hook-form";

// interface LoginFormInputs {
//   email: string;
//   password: string;
// }

// const Home = () => {
//   const {
//     register,
//     handleSubmit,
//     formState: { errors, isSubmitting },
//     setError,
//   } = useForm<LoginFormInputs>({
//     mode: "onTouched", // Validate on blur
//     reValidateMode: "onChange", // Re-validate on change
//   });
//   const [serverError, setServerError] = useState("");
//   const navigate = useNavigate();

//   const onSubmit: SubmitHandler<LoginFormInputs> = async (data) => {
//     setServerError("");
//     try {
//       const response = await fetch("http://localhost:8080/users/login", {
//         method: "POST",
//         headers: {
//           "Content-Type": "application/json",
//         },
//         body: JSON.stringify(data),
//       });

//       if (!response.ok) {
//         const errorData = await response.json();
//         throw new Error(errorData.message || "Invalid email or password");
//       }

//       const responseData = await response.json();
//       console.log("Login successful:", responseData);
//       setTimeout(() => {
//         navigate("/");
//       }, 2000);
//     } catch (error) {
//       console.error("Login failed:", error);
//       setServerError((error as Error).message || "Invalid email or password");
//     }
//   };

//   return (
//     <Container
//       maxWidth="xl"
//       style={{
//         minHeight: "100vh",
//         display: "flex",
//         flexDirection: "column",
//         justifyContent: "center",
//         alignItems: "center",
//         padding: "20px",
//         // background: "#f0f0f0",
//       }}
//     >
//       <form
//         onSubmit={handleSubmit(onSubmit)}
//         style={{
//           maxWidth: "400px",
//           width: "100%",
//           padding: "24px",
//           borderRadius: "8px",
//           marginTop: "95px",
//           marginBottom: "20px",
//           background: "white",
//         }}
//       >
//         <Typography
//           variant="h5"
//           align="center"
//           style={{ fontWeight: "bold", fontSize: "32px" }}
//           gutterBottom
//         >
//           LOGIN
//         </Typography>
//         <Grid container spacing={2}>
//           <Grid item xs={12}>
//             <TextField
//               fullWidth
//               variant="outlined"
//               label="Email"
//               {...register("email", {
//                 required: "Email is required",
//                 pattern: {
//                   value: /[a-z0-9._%+-]+@[a-z]+\.[a-z]{2,}$/,
//                   message: "Email format is invalid",
//                 },
//               })}
//               error={Boolean(errors.email)}
//               helperText={errors.email?.message}
//             />
//           </Grid>
//           <Grid item xs={12}>
//             <TextField
//               fullWidth
//               variant="outlined"
//               type="password"
//               label="Password"
//               {...register("password", { required: "Password is required" })}
//               error={Boolean(errors.password)}
//               helperText={errors.password?.message}
//             />
//           </Grid>
//           <Grid item xs={12}>
//             {serverError && (
//               <Typography variant="body2" color="error">
//                 {serverError}
//               </Typography>
//             )}
//           </Grid>
//           <Grid item xs={12}>
//             <Link
//               component={RouterLink}
//               to="/forgotPassword"
//               variant="body2"
//               style={{
//                 fontSize: "0.875rem",
//                 alignSelf: "flex-end",
//               }}
//             >
//               Forgot password?
//             </Link>
//           </Grid>
//           <Grid item xs={12}>
//             <Button
//               type="submit"
//               variant="contained"
//               color="success"
//               fullWidth
//               disabled={isSubmitting}
//               style={{
//                 marginTop: "20px",
//                 textTransform: "uppercase",
//                 fontWeight: "bold",
//               }}
//             >
//               {isSubmitting ? (
//                 <CircularProgress size={24} color="inherit" />
//               ) : (
//                 "Login"
//               )}
//             </Button>
//           </Grid>
//         </Grid>
//       </form>
//       <footer
//         style={{
//           width: "100%",
//           background: "#3f51b5",
//           color: "white",
//           padding: "16px",
//           textAlign: "center",
//           marginTop: "60px",
//         }}
//       >
//         <Typography variant="body2">
//           &copy; 2024 Your Company. All rights reserved.
//         </Typography>
//       </footer>
//     </Container>
//   );
// };

// export default Home;

import React, { useState } from "react";
import { Link as RouterLink, useNavigate } from "react-router-dom";
import {
  Container,
  Typography,
  TextField,
  Button,
  Link,
  CircularProgress,
  Grid,
} from "@mui/material";
import { useForm, SubmitHandler } from "react-hook-form";

interface LoginFormInputs {
  email: string;
  password: string;
}

const Home = () => {
  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm<LoginFormInputs>({
    mode: "onTouched", // Validate on blur
    reValidateMode: "onChange", // Re-validate on change
  });
  const [serverError, setServerError] = useState("");
  const navigate = useNavigate();

  const onSubmit: SubmitHandler<LoginFormInputs> = async (data) => {
    setServerError("");
    try {
      const response = await fetch("http://localhost:8080/users/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ email: data.email, password: data.password }),
      });

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || "Invalid email or password");
      }

      const responseData = await response.text();
      console.log("Login successful:", responseData);
      setTimeout(() => {
        navigate("/");
      }, 2000);
    } catch (error) {
      console.error("Login failed:", error);
      setServerError((error as Error).message || "Invalid email or password");
    }
  };

  return (
    <Container
      maxWidth="xl"
      style={{
        minHeight: "100vh",
        display: "flex",
        flexDirection: "column",
        justifyContent: "center",
        alignItems: "center",
        padding: "20px",
        // background: "#f0f0f0",
      }}
    >
      <form
        onSubmit={handleSubmit(onSubmit)}
        style={{
          maxWidth: "400px",
          width: "100%",
          padding: "24px",
          borderRadius: "8px",
          marginTop: "95px",
          marginBottom: "20px",
          background: "white",
        }}
      >
        <Typography
          variant="h5"
          align="center"
          style={{ fontWeight: "bold", fontSize: "32px" }}
          gutterBottom
        >
          LOGIN
        </Typography>
        <Grid container spacing={2}>
          <Grid item xs={12}>
            <TextField
              fullWidth
              variant="outlined"
              label="Email"
              {...register("email", {
                required: "Email is required",
                pattern: {
                  value: /[a-z0-9._%+-]+@[a-z]+\.[a-z]{2,}$/,
                  message: "Email format is invalid",
                },
              })}
              error={Boolean(errors.email)}
              helperText={errors.email?.message}
            />
          </Grid>
          <Grid item xs={12}>
            <TextField
              fullWidth
              variant="outlined"
              type="password"
              label="Password"
              {...register("password", { required: "Password is required" })}
              error={Boolean(errors.password)}
              helperText={errors.password?.message}
            />
          </Grid>
          <Grid item xs={12}>
            {serverError && (
              <Typography variant="body2" color="error">
                {serverError}
              </Typography>
            )}
          </Grid>
          <Grid item xs={12}>
            <Link
              component={RouterLink}
              to="/forgotPassword"
              variant="body2"
              style={{
                fontSize: "0.875rem",
                alignSelf: "flex-end",
              }}
            >
              Forgot password?
            </Link>
          </Grid>
          <Grid item xs={12}>
            <Button
              type="submit"
              variant="contained"
              color="success"
              fullWidth
              disabled={isSubmitting}
              style={{
                marginTop: "20px",
                textTransform: "uppercase",
                fontWeight: "bold",
              }}
            >
              {isSubmitting ? (
                <CircularProgress size={24} color="inherit" />
              ) : (
                "Login"
              )}
            </Button>
          </Grid>
        </Grid>
      </form>
      <footer
        style={{
          width: "100%",
          background: "#3f51b5",
          color: "white",
          padding: "16px",
          textAlign: "center",
          marginTop: "60px",
        }}
      >
        <Typography variant="body2">
          &copy; 2024 Your Company. All rights reserved.
        </Typography>
      </footer>
    </Container>
  );
};

export default Home;
