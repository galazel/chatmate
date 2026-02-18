import React from 'react'
import {LoginForm} from '../components/login-form'
import {SignupForm} from '../components/signup-form'

function SetupAccountPage() {
    const [isLogin, setIsLogin] = React.useState(true)
  return (
      <div className="flex min-h-svh w-full items-center justify-center p-6 md:p-10">
      <div className="w-full max-w-sm">
        {
           isLogin ? <LoginForm setIsLogin={setIsLogin}/> : <SignupForm setIsLogin={setIsLogin}/>
        }
      </div>
    </div>
  )
}

export default SetupAccountPage
