import React, { useState, useEffect } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import axios from "axios";
import {
  Container,
  Typography,
  TextField,
  Button,
  Alert,
  CircularProgress,
  Grid,
} from "@mui/material";
import { useForm, SubmitHandler } from "react-hook-form";

interface OTPPageProps {
  email: string;
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  formData?: any;
  verificationType: "register" | "forgotPassword";
}

interface FormValues {
  otp: string;
}

const OTPPage: React.FC = () => {
  const location = useLocation();
  const { state } = location;
  const { email, formData, verificationType } = state as OTPPageProps;

  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
    setError,
  } = useForm<FormValues>();
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const [successMessage, setSuccessMessage] = useState<string | null>(null);
  const [otpResent, setOtpResent] = useState<boolean>(false);
  const [otpExpiryTime, setOtpExpiryTime] = useState<Date | null>(null);
  const [resendTimer, setResendTimer] = useState<number>(60); // Timer for resend button
  const navigate = useNavigate();

  // Function to request OTP resend
  const resendOtp = async () => {
    try {
      if (verificationType === "register") {
        const resendResponse = await axios.post(
          `http://localhost:8080/authentication/validate`,
          { email }
        );
        setOtpResent(true);
        console.log(otpResent);
        const response = await resendResponse.data;
        if (resendResponse.status === 200) {
          setSuccessMessage(
            response.message || "OTP has been resent to your email."
          );
          setOtpExpiryTime(new Date(new Date().getTime() + 60 * 1000)); // Reset OTP expiry time to 1 minute from now
          setResendTimer(60); // Reset resend timer to 60 seconds
          setTimeout(() => {
            setOtpResent(false);
            setSuccessMessage(null);
          }, 3000);
        } else if (resendResponse.status === 400) {
          setErrorMessage(
            response.message ||
              "Email is not registered. Please register first."
          );
        }
      } else if (verificationType === "forgotPassword") {
        await axios.post("http://localhost:8080/authentication/resend-otp", {
          email,
        });
        setOtpResent(true);
        setSuccessMessage("OTP has been resent to your email.");
        setOtpExpiryTime(new Date(new Date().getTime() + 60 * 1000)); // Reset OTP expiry time to 1 minute from now
        setResendTimer(60); // Reset resend timer to 60 seconds
        setTimeout(() => {
          setOtpResent(false);
          setSuccessMessage(null);
        }, 3000);
      }
    } catch (error) {
      if (axios.isAxiosError(error)) {
        setErrorMessage(
          error.response?.data?.message || "An unexpected error occurred."
        );
      } else {
        setErrorMessage("An unexpected error occurred.");
      }
    }
  };

  // Function to check OTP expiry and handle resend timer
  useEffect(() => {
    const interval = setInterval(() => {
      if (otpExpiryTime && new Date() > otpExpiryTime) {
        setErrorMessage("OTP has expired. Please request a new one.");
      }
      setResendTimer((prev) => (prev > 0 ? prev - 1 : prev));
    }, 1000);

    return () => clearInterval(interval);
  }, [otpExpiryTime]);

  const onSubmit: SubmitHandler<FormValues> = async (data) => {
    try {
      if (verificationType === "register") {
        const formDataToSubmit = new FormData();
        Object.keys(formData).forEach((key) => {
          if (key !== "image") {
            formDataToSubmit.append(key, formData[key]);
          }
        });
        if (formData.image[0]) {
          formDataToSubmit.append("image", formData.image[0]);
        }
        formDataToSubmit.append("otp", data.otp);
        formData.otp = parseInt(data.otp);
        console.log(formData);

        const response = await axios.post(
          "http://localhost:8080/users",
          formData,
          {
            headers: {
              "Content-Type": "multipart/form-data",
            },
          }
        );

        if (response.status === 201) {
          setSuccessMessage(response.data.developerMessage);
          setTimeout(() => navigate("/login"), 2000);
        }
      } else if (verificationType === "forgotPassword") {
        const response = await axios.post(
          `http://localhost:8080/authentication/verify-otp`,
          {
            email,
            otp: data.otp,
          }
        );

        if (response.status === 200) {
          navigate("/resetPassword", { state: { email } });
        }
      }
    } catch (error) {
      console.log(error);
      if (axios.isAxiosError(error)) {
        if (error.response?.status === 400) {
          setError("otp", {
            type: "manual",
            message: "Invalid OTP. Please try again.",
          });
        } else if (error.response?.status === 410) {
          setErrorMessage("OTP has expired. Please request a new one.");
        } else {
          setErrorMessage(
            error.response?.data?.message || "An unexpected error occurred."
          );
        }
      }
    }
  };

  useEffect(() => {
    setOtpExpiryTime(new Date(new Date().getTime() + 5 * 60 * 1000)); // Set OTP expiry time to 5 minutes from now
  }, []);

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
          maxWidth: "500px",
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
          Verify OTP
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
            <Grid item xs={12}>
              <TextField
                {...register("otp", {
                  required: "OTP is required",
                  pattern: {
                    value: /^[0-9]+$/,
                    message: "OTP must contain only numbers",
                  },
                  maxLength: {
                    value: 6,
                    message: "OTP must be 6 digits long",
                  },
                })}
                label="OTP"
                error={Boolean(errors.otp)}
                helperText={errors.otp?.message as React.ReactNode}
                fullWidth
                inputProps={{ maxLength: 6 }}
              />
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
                {isSubmitting ? <CircularProgress size={24} /> : "Verify OTP"}
              </Button>
            </Grid>
            <Grid
              item
              xs={12}
              style={{ textAlign: "center", marginTop: "20px" }}
            >
              <Typography variant="body2">
                Resend OTP in {resendTimer} seconds
              </Typography>
              <Button
                variant="text"
                color="primary"
                onClick={resendOtp}
                disabled={resendTimer > 0 || isSubmitting}
              >
                Resend OTP
              </Button>
            </Grid>
          </Grid>
        </form>
      </Container>
    </div>
  );
};

export default OTPPage;
