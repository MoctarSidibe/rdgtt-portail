import React, { useState } from 'react';
import axios from 'axios';

const AirtelMoneyPayment = ({ payment, onPaymentSuccess, onPaymentError }) => {
    const [phoneNumber, setPhoneNumber] = useState('');
    const [pin, setPin] = useState('');
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError(null);

        try {
            const response = await axios.post(`/api/airtel-money/simulate-payment/${payment.id}`, {
                phoneNumber,
                pin
            });

            if (response.data.success) {
                onPaymentSuccess(response.data);
            } else {
                setError(response.data.message);
                onPaymentError(response.data);
            }
        } catch (err) {
            const errorMessage = err.response?.data?.message || 'Erreur lors du paiement';
            setError(errorMessage);
            onPaymentError({ message: errorMessage });
        } finally {
            setLoading(false);
        }
    };

    const formatPhoneNumber = (value) => {
        // Format gabonais: +241XXXXXXXX
        const cleaned = value.replace(/\D/g, '');
        if (cleaned.startsWith('241')) {
            return '+' + cleaned;
        } else if (cleaned.startsWith('0')) {
            return '+241' + cleaned.substring(1);
        } else if (cleaned.length > 0) {
            return '+241' + cleaned;
        }
        return cleaned;
    };

    return (
        <div className="bg-white p-6 rounded-lg shadow-md">
            <h3 className="text-xl font-semibold mb-4 text-gray-800">
                Paiement Airtel Money
            </h3>
            
            <div className="mb-4 p-4 bg-blue-50 rounded-lg">
                <h4 className="font-medium text-blue-800 mb-2">Informations du paiement</h4>
                <p className="text-sm text-blue-700">
                    <strong>Montant:</strong> {payment.montant} {payment.devise}
                </p>
                <p className="text-sm text-blue-700">
                    <strong>Référence:</strong> {payment.referencePaiement}
                </p>
                <p className="text-sm text-blue-700">
                    <strong>Date d'expiration:</strong> {new Date(payment.dateExpiration).toLocaleString()}
                </p>
            </div>

            <form onSubmit={handleSubmit} className="space-y-4">
                <div>
                    <label htmlFor="phoneNumber" className="block text-sm font-medium text-gray-700 mb-1">
                        Numéro de téléphone Airtel Money
                    </label>
                    <input
                        type="tel"
                        id="phoneNumber"
                        value={phoneNumber}
                        onChange={(e) => setPhoneNumber(formatPhoneNumber(e.target.value))}
                        placeholder="+241XXXXXXXX"
                        className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                        required
                        maxLength="13"
                    />
                    <p className="text-xs text-gray-500 mt-1">
                        Format: +241XXXXXXXX (9 chiffres après +241)
                    </p>
                </div>

                <div>
                    <label htmlFor="pin" className="block text-sm font-medium text-gray-700 mb-1">
                        PIN Airtel Money
                    </label>
                    <input
                        type="password"
                        id="pin"
                        value={pin}
                        onChange={(e) => setPin(e.target.value.replace(/\D/g, '').substring(0, 4))}
                        placeholder="1234"
                        className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                        required
                        maxLength="4"
                    />
                    <p className="text-xs text-gray-500 mt-1">
                        4 chiffres
                    </p>
                </div>

                {error && (
                    <div className="p-3 bg-red-50 border border-red-200 rounded-md">
                        <p className="text-sm text-red-600">{error}</p>
                    </div>
                )}

                <div className="bg-yellow-50 border border-yellow-200 rounded-md p-3">
                    <p className="text-sm text-yellow-700">
                        <strong>Mode Simulation:</strong> Ce paiement est simulé pour les tests. 
                        Aucun paiement réel ne sera effectué.
                    </p>
                </div>

                <button
                    type="submit"
                    disabled={loading || !phoneNumber || !pin}
                    className="w-full bg-green-600 text-white py-2 px-4 rounded-md hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-green-500 disabled:bg-gray-400 disabled:cursor-not-allowed"
                >
                    {loading ? 'Traitement en cours...' : 'Effectuer le paiement'}
                </button>
            </form>

            <div className="mt-4 text-xs text-gray-500">
                <p>En effectuant ce paiement, vous acceptez les conditions d'utilisation d'Airtel Money.</p>
            </div>
        </div>
    );
};

export default AirtelMoneyPayment;
