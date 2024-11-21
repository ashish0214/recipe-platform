import React, { ReactNode, createContext, useContext, useState } from 'react';

// Define the shape of the context data
interface UserContextData {
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  user: Record<string, any> | null;
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  setUser: (user: Record<string, any>) => void;
  otp: string | null;
  setOtp: (otp: string | null) => void;

}
interface UserProviderProps {
    children: ReactNode;
  }

// Create the context
const UserContext = createContext<UserContextData | undefined>(undefined);


export const UserProvider: React.FC<UserProviderProps> = ({ children}) => {
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  const [user, setUser] = useState<Record<string, any> | null>(null);
  const [otp, setOtp] = useState<string | null>(null);

  return (
    <UserContext.Provider value={{ user, otp, setUser, setOtp}}>
      {children}
    </UserContext.Provider>
  );
};

// Create a hook to use the context
// eslint-disable-next-line react-refresh/only-export-components
export const useUser = (): UserContextData => {
  const context = useContext(UserContext);
  if (!context) {
    throw new Error('useUser must be used within a UserProvider');
  }
  return context;
};
