// src/components/ui/alert.tsx
import * as React from "react"

interface AlertProps {
    variant?: 'default' | 'destructive';
    className?: string;
    children?: React.ReactNode;
}

export const Alert = React.forwardRef<HTMLDivElement, AlertProps>(
    ({ variant = 'default', className = '', children, ...props }, ref) => (
        <div
            ref={ref}
            role="alert"
            className={`relative w-full rounded-lg border p-4 ${
                variant === 'destructive' ? 'border-red-500 text-red-600 bg-red-50' : 'bg-white border-gray-200'
            } ${className}`}
            {...props}
        >
            {children}
        </div>
    )
)
Alert.displayName = "Alert"

export const AlertDescription = React.forwardRef<HTMLParagraphElement, React.HTMLAttributes<HTMLParagraphElement>>(
    ({ className = '', children, ...props }, ref) => (
        <div
            ref={ref}
            className={`text-sm ${className}`}
            {...props}
        >
            {children}
        </div>
    )
)
AlertDescription.displayName = "AlertDescription"