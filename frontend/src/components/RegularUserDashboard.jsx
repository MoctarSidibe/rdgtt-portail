import React, { useState, useEffect } from 'react';
import { 
  Card, 
  CardContent, 
  CardHeader, 
  CardTitle 
} from '@/components/ui/card';
import { 
  Badge, 
  Button, 
  Progress, 
  Tabs, 
  TabsContent, 
  TabsList, 
  TabsTrigger 
} from '@/components/ui';
import { 
  FileText, 
  Clock, 
  CheckCircle, 
  XCircle, 
  AlertCircle,
  Bell,
  CreditCard,
  Upload,
  Eye,
  Download,
  Car,
  FileCheck,
  Building,
  User
} from 'lucide-react';

const RegularUserDashboard = ({ user }) => {
  const [applications, setApplications] = useState([]);
  const [notifications, setNotifications] = useState([]);
  const [documents, setDocuments] = useState([]);
  const [payments, setPayments] = useState([]);
  const [availableServices, setAvailableServices] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchDashboardData();
  }, []);

  const fetchDashboardData = async () => {
    try {
      const [appsRes, notifRes, docsRes, payRes, servicesRes] = await Promise.all([
        fetch(`/api/user/applications?userId=${user.id}`),
        fetch(`/api/user/notifications?userId=${user.id}`),
        fetch(`/api/user/documents?userId=${user.id}`),
        fetch(`/api/user/payments?userId=${user.id}`),
        fetch(`/api/user/available-services`)
      ]);

      const [apps, notifs, docs, pays, services] = await Promise.all([
        appsRes.json(),
        notifRes.json(),
        docsRes.json(),
        payRes.json(),
        servicesRes.json()
      ]);

      setApplications(apps);
      setNotifications(notifs);
      setDocuments(docs);
      setPayments(pays);
      setAvailableServices(services);
    } catch (error) {
      console.error('Error fetching dashboard data:', error);
    } finally {
      setLoading(false);
    }
  };

  const getStatusColor = (status) => {
    const colors = {
      'DEPOSEE': 'bg-blue-100 text-blue-800',
      'EN_COURS': 'bg-yellow-100 text-yellow-800',
      'DOCUMENTS_MANQUANTS': 'bg-orange-100 text-orange-800',
      'EN_ATTENTE_PAIEMENT': 'bg-red-100 text-red-800',
      'VALIDATION': 'bg-purple-100 text-purple-800',
      'VALIDE': 'bg-green-100 text-green-800',
      'REJETEE': 'bg-red-100 text-red-800',
      'TERMINEE': 'bg-teal-100 text-teal-800'
    };
    return colors[status] || 'bg-gray-100 text-gray-800';
  };

  const getStatusIcon = (status) => {
    const icons = {
      'DEPOSEE': <Clock className="h-4 w-4" />,
      'EN_COURS': <Clock className="h-4 w-4" />,
      'DOCUMENTS_MANQUANTS': <AlertCircle className="h-4 w-4" />,
      'EN_ATTENTE_PAIEMENT': <CreditCard className="h-4 w-4" />,
      'VALIDATION': <Clock className="h-4 w-4" />,
      'VALIDE': <CheckCircle className="h-4 w-4" />,
      'REJETEE': <XCircle className="h-4 w-4" />,
      'TERMINEE': <CheckCircle className="h-4 w-4" />
    };
    return icons[status] || <Clock className="h-4 w-4" />;
  };

  const getServiceIcon = (serviceCode) => {
    const icons = {
      'permis': <Car className="h-6 w-6" />,
      'carte-grise': <FileCheck className="h-6 w-6" />,
      'transport': <Building className="h-6 w-6" />,
      'auto-ecole': <User className="h-6 w-6" />
    };
    return icons[serviceCode] || <FileText className="h-6 w-6" />;
  };

  const calculateProgress = (application) => {
    const statusOrder = ['DEPOSEE', 'EN_COURS', 'DOCUMENTS_MANQUANTS', 'EN_ATTENTE_PAIEMENT', 'VALIDATION', 'VALIDE', 'TERMINEE'];
    const currentIndex = statusOrder.indexOf(application.statut);
    return ((currentIndex + 1) / statusOrder.length) * 100;
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">
            Bonjour, {user.prenom} {user.nom}
          </h1>
          <p className="text-gray-600">
            Gérez vos demandes et suivez vos documents en temps réel
          </p>
        </div>
        <div className="flex items-center space-x-2">
          <Bell className="h-5 w-5 text-gray-400" />
          <span className="text-sm text-gray-600">
            {notifications.filter(n => !n.lu).length} notifications non lues
          </span>
        </div>
      </div>

      {/* Quick Stats */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
        <Card>
          <CardContent className="p-4">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-gray-600">Demandes actives</p>
                <p className="text-2xl font-bold text-blue-600">
                  {applications.filter(app => !['TERMINEE', 'REJETEE'].includes(app.statut)).length}
                </p>
              </div>
              <FileText className="h-8 w-8 text-blue-600" />
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardContent className="p-4">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-gray-600">Documents en attente</p>
                <p className="text-2xl font-bold text-orange-600">
                  {documents.filter(doc => doc.statut === 'EN_ATTENTE').length}
                </p>
              </div>
              <Upload className="h-8 w-8 text-orange-600" />
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardContent className="p-4">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-gray-600">Paiements en attente</p>
                <p className="text-2xl font-bold text-red-600">
                  {payments.filter(pay => pay.statut === 'EN_ATTENTE').length}
                </p>
              </div>
              <CreditCard className="h-8 w-8 text-red-600" />
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardContent className="p-4">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-gray-600">Notifications</p>
                <p className="text-2xl font-bold text-purple-600">
                  {notifications.filter(n => !n.lu).length}
                </p>
              </div>
              <Bell className="h-8 w-8 text-purple-600" />
            </div>
          </CardContent>
        </Card>
      </div>

      {/* Available Services */}
      <Card>
        <CardHeader>
          <CardTitle>Services Disponibles</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
            {availableServices.map((service) => (
              <Card key={service.id} className="cursor-pointer hover:shadow-md transition-shadow">
                <CardContent className="p-4 text-center">
                  <div className="flex justify-center mb-3">
                    {getServiceIcon(service.service_code)}
                  </div>
                  <h3 className="font-semibold text-gray-900 mb-2">
                    {service.nom}
                  </h3>
                  <p className="text-sm text-gray-600 mb-3">
                    {service.description}
                  </p>
                  <div className="text-sm text-gray-500 mb-3">
                    <p>Délai: {service.delai_traitement_jours} jours</p>
                    <p>Frais: {service.montant_frais?.toLocaleString()} XAF</p>
                  </div>
                  <Button className="w-full" size="sm">
                    Nouvelle demande
                  </Button>
                </CardContent>
              </Card>
            ))}
          </div>
        </CardContent>
      </Card>

      {/* Main Content */}
      <Tabs defaultValue="applications" className="space-y-4">
        <TabsList>
          <TabsTrigger value="applications">Mes Demandes</TabsTrigger>
          <TabsTrigger value="documents">Documents</TabsTrigger>
          <TabsTrigger value="payments">Paiements</TabsTrigger>
          <TabsTrigger value="notifications">Notifications</TabsTrigger>
        </TabsList>

        {/* Applications Tab */}
        <TabsContent value="applications" className="space-y-4">
          {applications.length === 0 ? (
            <Card>
              <CardContent className="p-8 text-center">
                <FileText className="h-12 w-12 text-gray-400 mx-auto mb-4" />
                <h3 className="text-lg font-medium text-gray-900 mb-2">
                  Aucune demande
                </h3>
                <p className="text-gray-600 mb-4">
                  Vous n'avez pas encore déposé de demande.
                </p>
                <Button>Nouvelle demande</Button>
              </CardContent>
            </Card>
          ) : (
            applications.map((application) => (
              <Card key={application.id}>
                <CardContent className="p-6">
                  <div className="flex items-center justify-between mb-4">
                    <div>
                      <h3 className="text-lg font-semibold text-gray-900">
                        {application.document_type?.nom}
                      </h3>
                      <p className="text-sm text-gray-600">
                        N° {application.numero_demande}
                      </p>
                    </div>
                    <Badge className={getStatusColor(application.statut)}>
                      <div className="flex items-center space-x-1">
                        {getStatusIcon(application.statut)}
                        <span>{application.statut}</span>
                      </div>
                    </Badge>
                  </div>

                  <div className="mb-4">
                    <div className="flex items-center justify-between text-sm text-gray-600 mb-2">
                      <span>Progression</span>
                      <span>{Math.round(calculateProgress(application))}%</span>
                    </div>
                    <Progress value={calculateProgress(application)} className="h-2" />
                  </div>

                  <div className="grid grid-cols-1 md:grid-cols-3 gap-4 text-sm">
                    <div>
                      <p className="text-gray-600">Date de dépôt</p>
                      <p className="font-medium">
                        {new Date(application.date_depot).toLocaleDateString('fr-FR')}
                      </p>
                    </div>
                    <div>
                      <p className="text-gray-600">Délai estimé</p>
                      <p className="font-medium">
                        {application.delai_estime_jours} jours
                      </p>
                    </div>
                    <div>
                      <p className="text-gray-600">Montant</p>
                      <p className="font-medium">
                        {application.montant_total?.toLocaleString()} XAF
                      </p>
                    </div>
                  </div>

                  <div className="flex items-center justify-between mt-4">
                    <Button variant="outline" size="sm">
                      <Eye className="h-4 w-4 mr-2" />
                      Voir détails
                    </Button>
                    {application.statut === 'TERMINEE' && (
                      <Button size="sm">
                        <Download className="h-4 w-4 mr-2" />
                        Télécharger
                      </Button>
                    )}
                  </div>
                </CardContent>
              </Card>
            ))
          )}
        </TabsContent>

        {/* Documents Tab */}
        <TabsContent value="documents" className="space-y-4">
          {documents.length === 0 ? (
            <Card>
              <CardContent className="p-8 text-center">
                <Upload className="h-12 w-12 text-gray-400 mx-auto mb-4" />
                <h3 className="text-lg font-medium text-gray-900 mb-2">
                  Aucun document
                </h3>
                <p className="text-gray-600">
                  Aucun document n'a été téléchargé pour le moment.
                </p>
              </CardContent>
            </Card>
          ) : (
            documents.map((document) => (
              <Card key={document.id}>
                <CardContent className="p-4">
                  <div className="flex items-center justify-between">
                    <div className="flex items-center space-x-3">
                      <FileText className="h-8 w-8 text-blue-600" />
                      <div>
                        <h4 className="font-medium text-gray-900">
                          {document.nom_original}
                        </h4>
                        <p className="text-sm text-gray-600">
                          {document.document_category?.nom}
                        </p>
                      </div>
                    </div>
                    <div className="flex items-center space-x-2">
                      <Badge className={getStatusColor(document.statut)}>
                        {document.statut}
                      </Badge>
                      <Button variant="outline" size="sm">
                        <Eye className="h-4 w-4" />
                      </Button>
                    </div>
                  </div>
                </CardContent>
              </Card>
            ))
          )}
        </TabsContent>

        {/* Payments Tab */}
        <TabsContent value="payments" className="space-y-4">
          {payments.length === 0 ? (
            <Card>
              <CardContent className="p-8 text-center">
                <CreditCard className="h-12 w-12 text-gray-400 mx-auto mb-4" />
                <h3 className="text-lg font-medium text-gray-900 mb-2">
                  Aucun paiement
                </h3>
                <p className="text-gray-600">
                  Aucun paiement n'a été effectué pour le moment.
                </p>
              </CardContent>
            </Card>
          ) : (
            payments.map((payment) => (
              <Card key={payment.id}>
                <CardContent className="p-4">
                  <div className="flex items-center justify-between">
                    <div>
                      <h4 className="font-medium text-gray-900">
                        {payment.document_type?.nom}
                      </h4>
                      <p className="text-sm text-gray-600">
                        {payment.montant.toLocaleString()} XAF
                      </p>
                    </div>
                    <div className="flex items-center space-x-2">
                      <Badge className={getStatusColor(payment.statut)}>
                        {payment.statut}
                      </Badge>
                      {payment.statut === 'EN_ATTENTE' && (
                        <Button size="sm">
                          Payer maintenant
                        </Button>
                      )}
                    </div>
                  </div>
                </CardContent>
              </Card>
            ))
          )}
        </TabsContent>

        {/* Notifications Tab */}
        <TabsContent value="notifications" className="space-y-4">
          {notifications.length === 0 ? (
            <Card>
              <CardContent className="p-8 text-center">
                <Bell className="h-12 w-12 text-gray-400 mx-auto mb-4" />
                <h3 className="text-lg font-medium text-gray-900 mb-2">
                  Aucune notification
                </h3>
                <p className="text-gray-600">
                  Vous n'avez pas encore de notifications.
                </p>
              </CardContent>
            </Card>
          ) : (
            notifications.map((notification) => (
              <Card key={notification.id} className={!notification.lu ? 'border-l-4 border-l-blue-500' : ''}>
                <CardContent className="p-4">
                  <div className="flex items-start justify-between">
                    <div className="flex-1">
                      <h4 className="font-medium text-gray-900 mb-1">
                        {notification.titre}
                      </h4>
                      <p className="text-sm text-gray-600 mb-2">
                        {notification.message}
                      </p>
                      <p className="text-xs text-gray-500">
                        {new Date(notification.created_at).toLocaleString('fr-FR')}
                      </p>
                    </div>
                    {!notification.lu && (
                      <div className="w-2 h-2 bg-blue-500 rounded-full ml-2"></div>
                    )}
                  </div>
                  {notification.action_requise && (
                    <div className="mt-3">
                      <Button size="sm">
                        {notification.action_text}
                      </Button>
                    </div>
                  )}
                </CardContent>
              </Card>
            ))
          )}
        </TabsContent>
      </Tabs>
    </div>
  );
};

export default RegularUserDashboard;
