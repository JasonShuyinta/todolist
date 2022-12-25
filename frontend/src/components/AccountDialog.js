import React, { useContext, useState } from "react";
import axios from "axios";
import {
  Typography,
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
  Button,
  TextField,
} from "@mui/material";
import { Context } from "../context/Context";
import { SERVER_URL } from "./Constants";

export default function AccountDialog({ open, handleClose }) {
  const { setUser, setConfig, setRefresh, refresh } = useContext(Context);

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [incorrectCredentials, setIncorrectCredentials] = useState(false);
  const [unexepectedError, setUnexepectedError] = useState(false);
  const [usernameTaken, setUsernameTaken] = useState(false);
  const [openSignup, setOpenSignup] = useState(false);

  const handleLogin = () => {
    axios
      .post(`${SERVER_URL}/auth/login`, { username, password })
      .then((res) => {
        if (res.status === 200) {
          setIncorrectCredentials(false);
          console.log(res);
          localStorage.setItem("todo-list-access-token", res.data.accessToken);
          setConfig({
            headers: {
              authorization: `Bearer ${res.data.accessToken}`,
            },
          });
          setUser(res.data);
          setUsername("");
          setPassword("");
          setRefresh(!refresh);
          handleClose();
        }
      })
      .catch((err) => {
        if (err.response.status === 409) {
          setIncorrectCredentials(true);
        }
      });
  };

  const handleSignup = () => {
    axios
      .post(`${SERVER_URL}/auth/signup`, { username, password })
      .then((res) => {
        if (res.status === 200) {
          setUnexepectedError(false);
          localStorage.setItem("todo-list-access-token", res.data.accessToken);
          setConfig({
            headers: {
              authorization: `Bearer ${res.data.accessToken}`,
            },
          });
          setUser(res.data);
          setUsername("");
          setPassword("");
          handleClose();
        } else {
          setUnexepectedError(true);
        }
      })
      .catch((err) => {
        if (err.response.status === 400) {
          setUsernameTaken(true);
        }
      });
  };

  return (
    <div>
      <Dialog open={open} onClose={handleClose}>
        <DialogTitle>{openSignup ? "Sign up" : "Login"}</DialogTitle>
        <DialogContent>
          <DialogContentText>
            {openSignup ? "Create a new account" : "Login to your account"}
          </DialogContentText>
          <TextField
            autoFocus
            margin="dense"
            id="name"
            label="Username"
            type="text"
            fullWidth
            variant="standard"
            required
            value={username}
            onChange={(e) => {
              setUsername(e.target.value);
              setUsernameTaken(false);
            }}
          />
          <TextField
            margin="dense"
            id="password"
            label="Password"
            type="password"
            fullWidth
            variant="standard"
            required
            value={password}
            onChange={(e) => {
              setPassword(e.target.value);
              setIncorrectCredentials(false);
            }}
          />
          {incorrectCredentials && (
            <Typography variant="body1" style={{ color: "red" }}>
              {" "}
              Incorrect credentials, please try again
            </Typography>
          )}
          {usernameTaken && (
            <Typography variant="body1" style={{ color: "red" }}>
              Username already taken, please try another one.
            </Typography>
          )}
          {unexepectedError && (
            <Typography variant="body1" style={{ color: "red" }}>
              Something went wrong, please retry later.
            </Typography>
          )}
          {openSignup ? (
            <Typography>
              Already have an account?
              <Button onClick={() => setOpenSignup(false)}>Login</Button>
            </Typography>
          ) : (
            <Typography>
              Don't have an account?{" "}
              <Button onClick={() => setOpenSignup(true)}>Sign up</Button>{" "}
            </Typography>
          )}
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose} color="secondary" variant="contained">
            Cancel
          </Button>
          {openSignup ? (
            <Button onClick={handleSignup} color="primary" variant="contained">
              Signup
            </Button>
          ) : (
            <Button onClick={handleLogin} color="primary" variant="contained">
              Login
            </Button>
          )}
        </DialogActions>
      </Dialog>
    </div>
  );
}
