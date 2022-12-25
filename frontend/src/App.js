import Navbar from "./components/Navbar";
import Main from "./components/Main";
import { ContextProvider } from "./context/Context";

function App() {
  return (
    <div className="App">
      <ContextProvider>
        <Navbar />
        <Main />
      </ContextProvider>
    </div>
  );
}

export default App;
