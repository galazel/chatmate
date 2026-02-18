import { Route, Routes } from "react-router-dom"
import SetupAccountPage from "./layouts/account-layout"
import MainChat from "./layouts/main-layout"

export function App() {
  return <Routes>
    <Route path="/" element={<div>Home</div>} />
    <Route path="/set-up" element={<SetupAccountPage/>} />
    <Route path="/main" element={<MainChat/>} />
    <Route path="*" element={<div>Not Found</div>} />
  </Routes>
}

export default App
